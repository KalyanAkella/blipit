package com.thoughtworks.blipit.persistance;

import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.UserPrefs;

public class AlertRepository {
    public void filterAlerts(GeoLocation userLocation, UserPrefs userPrefs, Utils.Handler<Alert> handler) {
        DataStoreHelper.retrieveAllAndProcess(Alert.class, handler);
    }
}
