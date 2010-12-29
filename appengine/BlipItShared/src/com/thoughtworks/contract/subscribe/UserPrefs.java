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

package com.thoughtworks.contract.subscribe;

import com.thoughtworks.contract.common.Channel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserPrefs implements Serializable {
    private double radius;
    private List<Channel> channels;

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public boolean isEmpty() {
        return getChannels() == null || getChannels().isEmpty();
    }

    public List<String> getChannelIds() {
        List<String> channelIds = new ArrayList<String>();
        for (Channel channel : getChannels()) {
            channelIds.add(channel.getId());
        }
        return channelIds;
    }
}
