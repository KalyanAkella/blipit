/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package com.thoughtworks.blipit.persistance;

import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.subscribe.UserPrefs;

import javax.jdo.Query;

public class AlertRepository {
    public void filterAlerts(GeoLocation userLocation, final UserPrefs userPrefs, Utils.ResultHandler<Alert> handler) {
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
