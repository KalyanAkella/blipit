package com.thoughtworks.blipit.activities;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;

public class UserLocationListener implements LocationListener {
    private BlipItActivity blipItActivity;

    public UserLocationListener(BlipItActivity blipItActivity) {
        this.blipItActivity = blipItActivity;
    }

    public void onLocationChanged(Location location) {
        int latitude = (int) (location.getLatitude() * 1E6);
        int longitude = (int) (location.getLongitude() * 1E6);
        blipItActivity.animateTo(new GeoPoint(latitude, longitude));
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onProviderEnabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onProviderDisabled(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
