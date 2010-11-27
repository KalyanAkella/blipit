package com.thoughtworks.blipit.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.maps.GeoPoint;
import com.thoughtworks.blipit.utils.BlipItConstants;

public class BlipNotificationServiceHandler extends Handler {
    private BlipNotificationService blipNotificationService;

    public BlipNotificationServiceHandler(BlipNotificationService blipNotificationService) {
        this.blipNotificationService = blipNotificationService;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BlipNotificationService.MSG_REGISTER_CLIENT:
                blipNotificationService.addClient(msg.replyTo);
                break;
            case BlipNotificationService.MSG_UNREGISTER_CLIENT:
                blipNotificationService.removeClient(msg.replyTo);
                break;
            case BlipNotificationService.MSG_USER_LOCATION_UPDATED:
                GeoPoint geoPoint = getGeoPointFromBundle(msg.getData());
                blipNotificationService.setCurrentUserLocation(geoPoint);
                break;
            default:
                super.handleMessage(msg);
        }
    }

    private GeoPoint getGeoPointFromBundle(Bundle bundle) {
        int latitude = bundle.getInt(BlipItConstants.USER_LOCATION_LATITUDE);
        int longitude = bundle.getInt(BlipItConstants.USER_LOCATION_LONGITUDE);
        return new GeoPoint(latitude, longitude);
    }
}
