package com.thoughtworks.blipit.utils;

import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import com.google.android.maps.GeoPoint;

public class BlipItUtils {
    public static final String USER_LOCATION_LATITUDE = "USER_LOCATION_LATITUDE";
    public static final String USER_LOCATION_LONGITUDE = "USER_LOCATION_LONGITUDE";
    public static final int MSG_REGISTER_CLIENT = 1;
    // TODO: handle unregister in the lifecycle methods of BlipItActivity
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_USER_LOCATION_UPDATED = 3;
    public static final int MSG_BLIPS_UPDATED = 4;

    private BlipItUtils() {
    }

    public static GeoPoint getGeoPointFromBundle(Bundle bundle) {
        if (!containsGeoPoint(bundle)) return null;
        int latitude = bundle.getInt(USER_LOCATION_LATITUDE);
        int longitude = bundle.getInt(USER_LOCATION_LONGITUDE);
        return new GeoPoint(latitude, longitude);
    }

    public static boolean containsGeoPoint(Bundle bundle) {
        return bundle != null && bundle.containsKey(USER_LOCATION_LATITUDE) && bundle.containsKey(USER_LOCATION_LONGITUDE);
    }

    public static void saveGeoPointInBundle(Bundle bundle, GeoPoint geoPoint) {
        bundle.putInt(USER_LOCATION_LATITUDE, geoPoint.getLatitudeE6());
        bundle.putInt(USER_LOCATION_LONGITUDE, geoPoint.getLongitudeE6());
    }

    public static Message getMessageWithGeoPoint(GeoPoint geoPoint, int messageId) {
        Message message = Message.obtain(null, messageId);
        Bundle bundle = new Bundle();
        bundle.putInt(USER_LOCATION_LATITUDE, geoPoint.getLatitudeE6());
        bundle.putInt(USER_LOCATION_LONGITUDE, geoPoint.getLongitudeE6());
        message.setData(bundle);
        return message;
    }

    public static GeoPoint asGeoPoint(Location location) {
        int latitude = (int) (location.getLatitude() * 1E6);
        int longitude = (int) (location.getLongitude() * 1E6);
        return new GeoPoint(latitude, longitude);
    }
}
