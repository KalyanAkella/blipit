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

package com.thoughtworks.blipit.filters;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.subscribe.GetBlipsRequest;

import java.util.ArrayList;
import java.util.List;

public class DistanceFilter implements BlipsFilter {

    private GeoLocation userLocation;
    private double preferredDistance;
    private DistanceCalculator distanceCalculator;

    public DistanceFilter(GeoLocation userLocation, double preferredDistance) {
        this.userLocation = userLocation;
        this.preferredDistance = preferredDistance;
        distanceCalculator = new DistanceCalculator();
    }

    public DistanceFilter(GetBlipsRequest getBlipsRequest) {
        this(getBlipsRequest.getUserLocation(), getBlipsRequest.getUserPrefs().getRadius());
    }

    public void doFilter(List<Blip> blips) {
        List<Blip> blipsOutsideDistanceLimit = new ArrayList<Blip>();
        for (Blip alert : blips) {
            GeoPt blipLocation = alert.getGeoPoint();
            GeoPt userLocation = Utils.asGeoPoint(this.userLocation);
//            double distanceFromUser = distanceCalculator.computeDistance1(userLocation, blipLocation);
            double distanceFromUser = distanceCalculator.computeDistance2(userLocation, blipLocation);
            if(distanceFromUser > preferredDistance) blipsOutsideDistanceLimit.add(alert);
        }
        blips.removeAll(blipsOutsideDistanceLimit);
    }
}