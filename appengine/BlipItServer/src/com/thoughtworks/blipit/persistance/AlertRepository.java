package com.thoughtworks.blipit.persistance;

import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.UserPrefs;

import javax.jdo.Query;

public class AlertRepository {
    public void filterAlerts(GeoLocation userLocation, UserPrefs userPrefs, Utils.Handler<Alert> handler) {
        DataStoreHelper.retrieveAllAndProcess(Alert.class, new Utils.QueryHandler() {
            public void prepare(Query query) {
                // write the filter here that takes in userLocation & userPrefs into account
            }
        }, handler);
    }
}
