package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.contract.GeoLocation;

public class GeoLocationToGeoPointConverter{

    public static GeoPt Convert(GeoLocation userLocation) {
        final Double latitude = userLocation.getLatitude();
        final Double longitude = userLocation.getLongitude();
        return new GeoPt(latitude.floatValue(), longitude.floatValue());
    }
}
