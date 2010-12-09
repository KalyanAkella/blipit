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

package com.thoughtworks.blipit.domain;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.thoughtworks.contract.Blip;
import com.thoughtworks.contract.GeoLocation;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.List;

@PersistenceCapable
public class Alert {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String source;

    @Persistent
    private String description;

    @Persistent
    private GeoPt geoPoint;

    @Persistent
    private List<String> channels;

    public Alert(String source, String description, GeoPt geoPoint, List<String> channels) {
        this.source = source;
        this.description = description;
        this.geoPoint = geoPoint;
        this.channels = channels;
    }

    public Key getKey() {
        return key;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }

    public GeoPt getGeoPoint() {
        return geoPoint;
    }

    public List<String> getChannels() {
        return channels;
    }

    public boolean isSameAs(Alert alert) {
        return this.getSource().equals(alert.getSource()) &&
                this.getDescription().equalsIgnoreCase(alert.getDescription()) &&
                this.getGeoPoint().compareTo(alert.getGeoPoint()) == 0;
    }

    public Blip toBlip() {
        Blip blip = new Blip();
        blip.setTitle(source);
        blip.setMessage(description);
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(geoPoint.getLatitude());
        geoLocation.setLongitude(geoPoint.getLongitude());
        blip.setBlipLocation(geoLocation);
        return blip;
    }
}
