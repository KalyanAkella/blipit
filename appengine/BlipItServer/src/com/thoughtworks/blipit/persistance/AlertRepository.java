package com.thoughtworks.blipit.persistance;

import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.UserPrefs;

import javax.jdo.Query;

public class AlertRepository {
    public void filterAlerts(GeoLocation userLocation, final UserPrefs userPrefs, Utils.Handler<Alert> handler) {
        DataStoreHelper.retrieveAllAndProcess(
                Alert.class,
                new Utils.QueryHandler() {
                    public void prepare(Query query) {
                        query.setFilter("userChannels.contains(channels)");
                        query.declareParameters("java.util.List userChannels");
                    }
                    public Object[] parameters() {
                        final Object[] parameters = new Object[1];
                        parameters[0] = userPrefs.getChannels();
                        return parameters;
                    }
                },
                handler);
    }
}
