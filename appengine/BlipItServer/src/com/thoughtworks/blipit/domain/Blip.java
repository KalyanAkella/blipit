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
import com.google.appengine.api.datastore.KeyFactory;
import com.thoughtworks.blipit.Utils;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable
public class Blip {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String title;

    @Persistent
    private String description;

    @Persistent
    private GeoPt geoPoint;

    @Persistent
    private Set<Key> channelKeys; // un-owned one-to-many relationship. a given channel can be associated with multiple Blips

    @Persistent
    private String creatorId;

    public Blip(String title, String description, GeoPt geoPoint, Set<Key> channelKeys, String creatorId) {
        this.title = title;
        this.description = description;
        this.geoPoint = geoPoint;
        this.channelKeys = channelKeys;
        this.creatorId = creatorId;
    }

    public Blip(String title, String description, GeoPt geoPoint) {
        this(title, description, geoPoint, new HashSet<Key>(), null);
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public GeoPt getGeoPoint() {
        return geoPoint;
    }

    public Set<Key> getChannelKeys() {
        return channelKeys;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGeoPoint(GeoPt geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Blip setChannelKeys(Set<Key> channelKeys) {
        this.channelKeys = channelKeys;
        return this;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Blip blip = (Blip) o;

        if (key != null ? !key.equals(blip.key) : blip.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    public void copy(Blip givenBlip) {
        setTitle(givenBlip.title);
        setDescription(givenBlip.description);
        setGeoPoint(givenBlip.geoPoint);
        setChannelKeys(givenBlip.channelKeys);
        setCreatorId(givenBlip.creatorId);
    }

    public void prepareChannelKeys() {
        Set<Key> channelKeys = new HashSet<Key>();
        if (Utils.isNotEmpty(this.channelKeys)) {
            for (Key channelKey : this.channelKeys) {
                channelKeys.add(KeyFactory.createKey(Channel.class.getSimpleName(), channelKey.getId()));
            }
        }
        this.channelKeys = channelKeys;
    }

    public void prepareKeys() {
        if (key != null) {
            this.key = KeyFactory.createKey(Blip.class.getSimpleName(), key.getId());
        }
        prepareChannelKeys();
    }
}
