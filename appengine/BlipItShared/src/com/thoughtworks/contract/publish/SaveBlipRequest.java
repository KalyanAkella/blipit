package com.thoughtworks.contract.publish;

import com.thoughtworks.contract.GeoLocation;

import java.io.Serializable;
import java.util.List;

public class SaveBlipRequest implements Serializable {
    private GeoLocation userLocation;
    private List<String> applicableChannels;

    public GeoLocation getBlipLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoLocation userLocation) {
        this.userLocation = userLocation;
    }

    public List<String> getApplicableChannels() {
        return applicableChannels;
    }

    public void setApplicableChannels(List<String> applicableChannels) {
        this.applicableChannels = applicableChannels;
    }
}
