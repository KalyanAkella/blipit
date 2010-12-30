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

package com.thoughtworks.blipit.alertFilters;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.contract.GeoLocation;

import java.util.ArrayList;
import java.util.List;

public class DistanceFilter implements IAlertFilter {

    private GeoLocation userLocation;
    private double distanceOfConvinience;
    private DistanceCalculator distanceCalculator;

    public DistanceFilter(GeoLocation userLocation, double distanceOfConvinience) {
        this.userLocation = userLocation;
        this.distanceOfConvinience = distanceOfConvinience;
        distanceCalculator = new DistanceCalculator();
    }

    public void apply(List<Blip> alerts) {
        final ArrayList<Blip> alertsOutsideDistanceOfConvinience = new ArrayList<Blip>();
        for (Blip alert : alerts) {
            final GeoPt alertPt = alert.getGeoPoint();
            final GeoPt userPt = Utils.asGeoPoint(userLocation);
            final double distanceFromUser = distanceCalculator.CalculationByDistance(userPt, alertPt);
            if(distanceFromUser > distanceOfConvinience)
                alertsOutsideDistanceOfConvinience.add(alert);
        }
        alerts.removeAll(alertsOutsideDistanceOfConvinience);
    }
}