package com.thoughtworks.contract;

import java.io.Serializable;

public class BlipItRequest implements Serializable {
    private GeoLocation userLocation;
    private UserPrefs userPrefs;

    public GeoLocation getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoLocation userLocation) {
        this.userLocation = userLocation;
    }

    public UserPrefs getUserPrefs() {
        return userPrefs;
    }

    public void setUserPrefs(UserPrefs userPrefs) {
        this.userPrefs = userPrefs;
    }
}
