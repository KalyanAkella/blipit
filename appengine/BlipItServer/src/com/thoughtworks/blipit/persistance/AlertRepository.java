package com.thoughtworks.blipit.persistance;

import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.UserPrefs;

import java.util.List;

public class AlertRepository {
    public List<Alert> GetAlerts(GeoLocation userLocation, UserPrefs userPrefs) throws Exception {
        return DataStoreHelper.retrieveAllAndProcess(Alert.class);
    }
}
