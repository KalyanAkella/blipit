package com.thoughtworks.blipit.utils;

import android.location.Location;
import com.thoughtworks.contract.GeoLocation;

public class PanicBlipUtils {
    public static GeoLocation getGeoLocation(Location lastKnownLocation) {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(lastKnownLocation.getLatitude());
        geoLocation.setLongitude(lastKnownLocation.getLongitude());
        return geoLocation;
    }
}
