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

package com.thoughtworks.blipit.types;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Blip implements Serializable {
    private static final String CREATOR_ID = "%s:%s";
    private Key key;
    private String title;
    private String description;
    private Location geoPoint;
    private String creatorId;
    private Set<Key> channelKeys;

    public Blip() {
        this(null, null, null, null, null, new HashSet<Key>());
    }

    public Blip(Key key, String title, String description, Location geoPoint, String creatorId, Set<Key> channelKeys) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.geoPoint = geoPoint;
        this.channelKeys = channelKeys;
        this.creatorId = creatorId;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(Location geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Set<Key> getChannelKeys() {
        return channelKeys;
    }

    public void setChannelKeys(Set<Key> channelKeys) {
        this.channelKeys = channelKeys;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void setCreatorId(String deviceId, String phoneNumber) {
        this.creatorId = String.format(CREATOR_ID, deviceId, phoneNumber);
    }

    public void clear() {
        this.channelKeys.clear();
    }

    public boolean isEmpty() {
        return channelKeys == null || channelKeys.isEmpty();
    }

    public boolean isSaved() {
        return this.key != null;
    }

    public void setChannelKeys(List<Channel> channels) {
        clear();
        for (Channel channel : channels) {
            this.channelKeys.add(channel.getKey());
        }
    }
}
