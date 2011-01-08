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

import java.util.HashSet;
import java.util.Set;

public class Filter {
    private Key key;
    private Location geoPoint;
    private float radius;
    private Set<Key> channelKeys;

    public Filter() {
        this(null, null, 2, new HashSet<Key>());
    }

    public Filter(Key key, Location geoPoint, float radius, Set<Key> channelKeys) {
        this.key = key;
        this.geoPoint = geoPoint;
        this.radius = radius;
        this.channelKeys = channelKeys;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Location getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(Location geoPoint) {
        this.geoPoint = geoPoint;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Set<Key> getChannelKeys() {
        return channelKeys;
    }

    public void setChannelKeys(Set<Key> channelKeys) {
        this.channelKeys = channelKeys;
    }

    public boolean isEmpty() {
        return channelKeys == null || channelKeys.isEmpty() || geoPoint == null || geoPoint.isEmpty();
    }
}
