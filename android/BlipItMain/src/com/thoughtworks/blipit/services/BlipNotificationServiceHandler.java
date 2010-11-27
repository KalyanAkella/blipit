package com.thoughtworks.blipit.services;

import android.os.Handler;
import android.os.Message;
import com.google.android.maps.GeoPoint;

import static com.thoughtworks.blipit.utils.BlipItUtils.MSG_REGISTER_CLIENT;
import static com.thoughtworks.blipit.utils.BlipItUtils.MSG_UNREGISTER_CLIENT;
import static com.thoughtworks.blipit.utils.BlipItUtils.MSG_USER_LOCATION_UPDATED;
import static com.thoughtworks.blipit.utils.BlipItUtils.getGeoPointFromBundle;

public class BlipNotificationServiceHandler extends Handler {
    private BlipNotificationService blipNotificationService;

    public BlipNotificationServiceHandler(BlipNotificationService blipNotificationService) {
        this.blipNotificationService = blipNotificationService;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REGISTER_CLIENT:
                blipNotificationService.addClient(msg.replyTo);
                break;
            case MSG_UNREGISTER_CLIENT:
                blipNotificationService.removeClient(msg.replyTo);
                break;
            case MSG_USER_LOCATION_UPDATED:
                GeoPoint geoPoint = getGeoPointFromBundle(msg.getData());
                blipNotificationService.setCurrentUserLocation(geoPoint);
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
