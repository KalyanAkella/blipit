package com.thoughtworks.contract;

import java.io.Serializable;

public class GeoLocation implements Serializable {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public int getLatitudeE6() {
        return (int) (latitude * 1E6);
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getLongitudeE6() {
        return (int) (longitude * 1E6);
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
