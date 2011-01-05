/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit.panicblip.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.thoughtworks.blipit.panicblip.types.Channel;
import com.thoughtworks.blipit.panicblip.types.Panic;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipHttpHelper;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.LOCATION_CHANGED;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.areSameStrings;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.getLocation;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.getMessage;

public class PanicNotificationService extends Service {
    private List<Messenger> clients;
    private String blipItServiceLoc;
    private Messenger panicMessenger;
    private int minTimeForLocUpdates;
    private LocationManager locationManager;
    private PendingIntent pendingIntentForLocUpdates;
    private Looper panicNotificationServiceLooper;
    private PanicNotificationServiceHandler panicNotificationServiceHandler;
    private Panic panicBlip;
    private Location currentBestLocation;

    public PanicNotificationService() {
        panicBlip = new Panic();
        clients = new ArrayList<Messenger>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        readServiceMetadata();
        readPhoneState();
        initHandlerThread();
        initLocationManager();
        Log.i(APP_TAG, "Panic notification service started");
    }

    private void readPhoneState() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        panicBlip.setCreatorId(telephonyManager.getDeviceId(), telephonyManager.getLine1Number());
        panicBlip.setTitle("Panic Request");
        panicBlip.setDescription("Panic Request from " + telephonyManager.getLine1Number());
    }

    private void initHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("PanicNotificationServiceThread");
        handlerThread.start();
        panicNotificationServiceLooper = handlerThread.getLooper();
        panicNotificationServiceHandler = new PanicNotificationServiceHandler(this, panicNotificationServiceLooper);
        panicMessenger = new Messenger(panicNotificationServiceHandler);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Message message = panicNotificationServiceHandler.obtainMessage();
        message.arg1 = startId;
        message.obj = intent;
        message.what = intent.getIntExtra(APP_TAG, -1);
        panicNotificationServiceHandler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(pendingIntentForLocUpdates);
        panicBlip.clear();
        panicNotificationServiceLooper.quit();
        Log.i(APP_TAG, "Panic notification service stopped");
    }

    private void initLocationManager() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        pendingIntentForLocUpdates = PendingIntent.getService(this, 0, getIntentForLocationUpdates(), 0);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeForLocUpdates, 0, pendingIntentForLocUpdates);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTimeForLocUpdates, 0, pendingIntentForLocUpdates);
    }

    private Intent getIntentForLocationUpdates() {
        Intent intent = new Intent(this, PanicNotificationService.class);
        intent.putExtra(APP_TAG, LOCATION_CHANGED);
        return intent;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return panicMessenger.getBinder();
    }

    public void onLocationChanged(Intent intent) {
        if (panicBlip == null || panicBlip.isEmpty()) return;
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey(LocationManager.KEY_LOCATION_CHANGED)) {
            Location newLocation = (Location) bundle.get(LocationManager.KEY_LOCATION_CHANGED);
            if (isBetterLocation(newLocation)) reportPanic(newLocation);
        }
    }

    /**
     * Algorithm that determines whether the current location fix is good enough. Based on the sample code obtained
     * from http://developer.android.com/guide/topics/location/obtaining-user-location.html
     *
     * @param location the location obtained in the current fix
     * @return true if the current fix is good enough compared to the earlier one, else returns false.
     */
    private boolean isBetterLocation(Location location) {
        boolean result;
        if (currentBestLocation == null) {
            result = true;
        } else {
            long timeDelta = location.getTime() - currentBestLocation.getTime();
            boolean isNewer = timeDelta > minTimeForLocUpdates;
            boolean isOlder = timeDelta < -minTimeForLocUpdates;

            if (isNewer) {
                result = true;
            } else if (isOlder) {
                result = false;
            } else {
                int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
                boolean isLessAccurate = accuracyDelta > 0;
                boolean isMoreAccurate = accuracyDelta < 0;
                boolean isVeryLessAccurate = accuracyDelta > 200;
                boolean isFromSameProvider = areSameStrings(location.getProvider(), currentBestLocation.getProvider());
                result = isMoreAccurate || !isLessAccurate || !isVeryLessAccurate && isFromSameProvider;
            }
        }
        if (result) currentBestLocation = location;
        return result;
    }

    public void reportPanic(List<Channel> channels) {
        panicBlip.setChannelKeys(channels);
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation == null) {
            notifyClients(PanicBlipUtils.REPORT_PANIC_LOC_ERROR, null);
        } else {
            reportPanic(lastKnownLocation);
        }
    }

    private void notifyClients(int messageId, Serializable data) {
        Message message = getMessage(messageId, data);
        for (Iterator<Messenger> iterator = clients.iterator(); iterator.hasNext();) {
            Messenger client = iterator.next();
            try {
                client.send(message);
            } catch (RemoteException e) {
                Log.e(APP_TAG, "An error occurred while sending message with ID: " + messageId + " to client", e);
                iterator.remove();
            }
        }
    }

    private Location getLastKnownLocation() {
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null)
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastKnownLocation == null)
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return lastKnownLocation;
    }

    private void reportPanic(Location newLocation) {
        try {
            panicBlip.setGeoPoint(getLocation(newLocation));
            Panic savedPanic = PanicBlipHttpHelper.getInstance().savePanic(blipItServiceLoc, panicBlip);
            if (savedPanic == null) {
                notifyClients(PanicBlipUtils.REPORT_PANIC_FAILURE, null);
            } else {
                panicBlip = savedPanic;
                notifyClients(PanicBlipUtils.REPORT_PANIC_SUCCESS, null);
            }
        } catch (Exception e) {
            notifyClients(PanicBlipUtils.REPORT_PANIC_FAILURE, e.getMessage());
            Log.e(APP_TAG, "Error occurred while saving panic blip", e);
        }
    }

    public void clearPanic() {
        if (panicBlip == null || panicBlip.isEmpty() || !panicBlip.isSaved()) return;
        try {
            boolean deleted = PanicBlipHttpHelper.getInstance().deletePanic(blipItServiceLoc, panicBlip);
            if (deleted) {
                notifyClients(PanicBlipUtils.CLEAR_PANIC_SUCCESS, null);
                stopSelf(); // stop the PanicNotificationService after clearing panic issue
            } else {
                notifyClients(PanicBlipUtils.CLEAR_PANIC_FAILURE, null);
            }
        } catch (Exception e) {
            notifyClients(PanicBlipUtils.CLEAR_PANIC_FAILURE, e.getMessage());
            Log.e(APP_TAG, "Error occurred while clearing the panic blip", e);
        }
    }

    private void readServiceMetadata() {
        try {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(this, PanicNotificationService.class);
            ServiceInfo serviceInfo = packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            Bundle metaData = serviceInfo.metaData;
            blipItServiceLoc = metaData.getString("blipit.service.loc");
            minTimeForLocUpdates = Integer.valueOf(metaData.getString("time.between.loc.updates.in.millis"));
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(APP_TAG, "Unable to retrieve service metadata", e);
        } catch (NumberFormatException e) {
            minTimeForLocUpdates = 0;
            Log.e(APP_TAG, "Unable to read value of min time between loc updates", e);
        }
    }

    public void addClient(Messenger clientMessenger) {
        this.clients.add(clientMessenger);
    }

    public void removeClient(Messenger messenger) {
        this.clients.remove(messenger);
    }
}
