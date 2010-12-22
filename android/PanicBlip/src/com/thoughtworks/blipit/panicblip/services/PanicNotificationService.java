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
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipServiceHelper;
import com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils;
import com.thoughtworks.contract.BlipItResponse;
import com.thoughtworks.contract.publish.BlipItPublishResource;
import com.thoughtworks.contract.publish.SaveBlipRequest;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.APP_TAG;
import static com.thoughtworks.blipit.panicblip.utils.PanicBlipUtils.LOCATION_CHANGED;

public class PanicNotificationService extends Service {
    private String blipItServiceUrl;
    private ConcurrentLinkedQueue<ArrayList<String>> panicQueue;
    private Messenger panicMessenger;
    private int minTimeForLocUpdates;
    private LocationManager locationManager;
    private PendingIntent pendingIntentForLocUpdates;
    private Looper panicNotificationServiceLooper;
    private PanicNotificationServiceHandler panicNotificationServiceHandler;

    public PanicNotificationService() {
        panicQueue = new ConcurrentLinkedQueue<ArrayList<String>>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        readServiceMetadata();
        initHandlerThread();
        initLocationManager();
        Log.i(APP_TAG, "Panic notification service started");
    }

    private void initHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("PanicNotificationServiceThread", Process.THREAD_PRIORITY_FOREGROUND);
        handlerThread.start();
        panicNotificationServiceLooper = handlerThread.getLooper();
        panicNotificationServiceHandler = new PanicNotificationServiceHandler(this, panicNotificationServiceLooper);
        panicMessenger = new Messenger(panicNotificationServiceHandler);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Message message = panicNotificationServiceHandler.obtainMessage();  // this just creates a fresh message - no magic here !!!
        message.arg1 = startId;
        message.obj = intent;
        message.what = intent.getIntExtra(APP_TAG, -1);
        panicNotificationServiceHandler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(pendingIntentForLocUpdates);
        panicQueue.clear();
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
        if (panicQueue.isEmpty()) return;
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey(LocationManager.KEY_LOCATION_CHANGED)) {
            // TODO: Determine if this is a good enough location reading
            Location newLocation = (Location) bundle.get(LocationManager.KEY_LOCATION_CHANGED);
            for (ArrayList<String> issueList : panicQueue) {
                reportIssue(newLocation, issueList);
            }
        }
    }

    public void reportIssue(ArrayList<String> topics) {
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation == null) {
            Toast.makeText(this, "Your issue will be reported as soon as we detect your current location", Toast.LENGTH_LONG).show();
        } else {
            reportIssue(lastKnownLocation, topics);
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

    private void reportIssue(Location newLocation, ArrayList<String> topics) {
        try {
            SaveBlipRequest saveBlipRequest = PanicBlipUtils.getSaveBlipRequest(newLocation, topics);
            BlipItPublishResource publishResource = PanicBlipServiceHelper.getPublishResource(blipItServiceUrl);
            BlipItResponse saveBlipResponse = publishResource.saveBlip(saveBlipRequest);
            if (saveBlipResponse.isFailure()) {
                Toast.makeText(this, saveBlipResponse.getBlipItError().getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Issue reported successfully", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "We are unable to report your issue at this time. Please try again.", Toast.LENGTH_LONG).show();
            Log.e(APP_TAG, "Error occurred while saving report", e);
        }
    }

    public void reportAndRegisterPanic(ArrayList<String> topics) {
        reportIssue(topics);
        panicQueue.add(topics);
    }

    public void clearAllIssues() {
        for (ArrayList<String> issueList : panicQueue) {
            // TODO: call the back-end service to remove panic here !
        }
        panicQueue.clear();
        stopSelf();
    }

    private void readServiceMetadata() {
        try {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(this, PanicNotificationService.class);
            ServiceInfo serviceInfo = packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            Bundle metaData = serviceInfo.metaData;
            blipItServiceUrl = metaData.getString("blipit.service.url");
            minTimeForLocUpdates = Integer.valueOf(metaData.getString("time.between.loc.updates.in.millis"));
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(APP_TAG, "Unable to retrieve service metadata", e);
        } catch (NumberFormatException e) {
            minTimeForLocUpdates = 0;
            Log.e(APP_TAG, "Unable to read value of min time between loc updates", e);
        }
    }
}
