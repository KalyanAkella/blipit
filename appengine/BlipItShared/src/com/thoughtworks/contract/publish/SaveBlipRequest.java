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

package com.thoughtworks.contract.publish;

import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.common.Channel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveBlipRequest implements Serializable {
    private GeoLocation userLocation;
    private List<Channel> channels;
    private Map<String, String> metaData;
    private String blipId;

    public SaveBlipRequest() {
        metaData = new HashMap<String, String>();
        channels = new ArrayList<Channel>();
    }

    public GeoLocation getBlipLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoLocation userLocation) {
        this.userLocation = userLocation;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public String getBlipId() {
        return blipId;
    }

    public void setBlipId(String blipId) {
        this.blipId = blipId;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public void addMetaData(String key, String value) {
        metaData.put(key, value);
    }

    public String getMetaDataValue(String key) {
        return metaData.get(key);
    }

    public boolean isEmpty() {
        return channels == null || channels.isEmpty();
    }

    public void clear() {
        channels.clear();
    }

    public boolean isPanicRequest() {
        return !isEmpty() && channels.get(0).getCategory().equals(Category.PANIC);
    }
}
