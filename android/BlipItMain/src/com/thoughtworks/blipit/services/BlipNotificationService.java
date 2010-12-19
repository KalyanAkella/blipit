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

package com.thoughtworks.blipit.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.maps.GeoPoint;
import com.thoughtworks.blipit.utils.BlipItServiceHelper;
import com.thoughtworks.blipit.utils.BlipItUtils;
import com.thoughtworks.contract.subscribe.Blip;
import com.thoughtworks.contract.subscribe.GetBlipsRequest;
import com.thoughtworks.contract.subscribe.GetBlipsResponse;
import com.thoughtworks.contract.subscribe.UserPrefs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.thoughtworks.blipit.utils.BlipItUtils.APP_TAG;
import static com.thoughtworks.blipit.utils.BlipItUtils.RADIUS_PREF_KEY;
import static com.thoughtworks.blipit.utils.BlipItUtils.getMessageWithBlips;

public class BlipNotificationService extends IntentService {
    private final List<Messenger> clients;
    private final Messenger clientMessenger;
    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    private GeoPoint currentUserLocation;
    private String blipItServiceUrl;

    public BlipNotificationService() {
        super("BlipNotificationServiceThread");
        clients = new ArrayList<Messenger>();
        clientMessenger = new Messenger(new BlipNotificationServiceHandler(this));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clientMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        readBlipItServiceUrl();
        scheduleNotificationService();
        Log.i(APP_TAG, "Blip notification service started");
    }

    private void readBlipItServiceUrl() {
        try {
            PackageManager packageManager = getPackageManager();
            ComponentName componentName = new ComponentName(this, BlipNotificationService.class);
            ServiceInfo serviceInfo = packageManager.getServiceInfo(componentName, PackageManager.GET_META_DATA);
            blipItServiceUrl = serviceInfo.metaData.getString("blipit.service.url");
        } catch (PackageManager.NameNotFoundException e) {
            Log.wtf(APP_TAG, "Unable to retrieve service metadata", e);
        }
    }

    private void scheduleNotificationService() {
        pendingIntent = PendingIntent.getService(this, 0, new Intent(this, BlipNotificationService.class), 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long firstTime = SystemClock.elapsedRealtime();
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 5 * 1000, pendingIntent);
    }

    @Override
    public void onDestroy() {
        alarmManager.cancel(pendingIntent);
        Log.i(APP_TAG, "Blip notification service stopped");
    }

    public void addClient(Messenger messenger) {
        this.clients.add(messenger);
    }

    public void removeClient(Messenger messenger) {
        this.clients.remove(messenger);
    }

    public synchronized void setCurrentUserLocation(GeoPoint currentUserLocation) {
        this.currentUserLocation = currentUserLocation;
        Log.i(APP_TAG, "User location updated");
    }

    public synchronized GeoPoint getCurrentUserLocation() {
        return currentUserLocation;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            GetBlipsRequest blipItRequest = constructRequest();
            GetBlipsResponse blipItResponse = BlipItServiceHelper.getSubscribeResource(blipItServiceUrl).getBlips(blipItRequest);
            if (blipItResponse.isSuccess()) {
                Message message = getMessageWithBlips((ArrayList<Blip>) blipItResponse.getBlips(), BlipItUtils.MSG_BLIPS_UPDATED);
                for (Iterator<Messenger> iterator = clients.iterator(); iterator.hasNext();) {
                    Messenger client = iterator.next();
                    try {
                        client.send(message);
                    } catch (RemoteException e) {
                        Log.e(BlipItUtils.APP_TAG, "An error occurred while sending blips to client", e);
                        iterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            Log.e(BlipItUtils.APP_TAG, "An error occurred while retrieving blips", e);
        }
    }

    private GetBlipsRequest constructRequest() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        float radius = BlipItUtils.getRadius(sharedPreferences, RADIUS_PREF_KEY);
        String channelPrefStr = sharedPreferences.getString(BlipItUtils.CHANNEL_PREF_KEY, null);
        Log.i(APP_TAG, "Preferences: Radius -> " + radius + ", Channels -> " + channelPrefStr);
        GetBlipsRequest blipItRequest = new GetBlipsRequest();
        blipItRequest.setUserLocation(BlipItUtils.toGeoLocation(getCurrentUserLocation()));
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setRadius(radius);
        userPrefs.setChannels(BlipItUtils.toChannelList(channelPrefStr));
        blipItRequest.setUserPrefs(userPrefs);
        return blipItRequest;
    }

}
