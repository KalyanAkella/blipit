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
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.contract.common.ChannelCategory;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.subscribe.UserPrefs;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlipItRepository {
    public void filterAlerts(GeoLocation userLocation, final UserPrefs userPrefs, Utils.ResultHandler<Alert> handler) {
        DataStoreHelper.retrieveAllAndProcess(
                Alert.class,
                new Utils.QueryHandler() {
                    public void prepare(Query query) {
                        query.setFilter("userChannels.contains(channels)");
                        query.declareParameters("java.util.List userChannels");
                    }

                    public Object[] parameters() {
                        return new Object[]{getChannelIds(userPrefs)};
                    }
                },
                handler);
    }

    private List<String> getChannelIds(UserPrefs userPrefs) {
        List<String> channelIds = new ArrayList<String>();
        for (com.thoughtworks.contract.common.Channel channel : userPrefs.getChannels()) {
            channelIds.add(channel.getId());
        }
        return channelIds;
    }

    public void retrieveChannelsByCategory(ChannelCategory channelCategory, final Utils.ResultHandler<Channel> handler) {
        List<Channel> channels = saveAndGetChannels(channelCategory, handler);
        for (Channel channel : channels) {
            handler.onSuccess(channel);
        }
    }

    // TODO: Currently saves & gets the channels. This should go away in future. Find out a way to insert reference data like Channels.
    private List<Channel> saveAndGetChannels(ChannelCategory channelCategory, final Utils.ResultHandler<Channel> handler) {
        List<String> channelNames = getChannelNames(channelCategory);
        final List<Channel> channels = new ArrayList<Channel>();
        for (String channelName : channelNames) {
            final Channel channel = new Channel();
            channel.setCategory(channelCategory);
            channel.setName(channelName);
            channel.setDescription(channelName);
            DataStoreHelper.save(channel, new Utils.ResultHandler<Channel>() {
                public void onSuccess(Channel arg) {
                    channels.add(channel);
                }

                public void onError(Throwable throwable) {
                    handler.onError(throwable);
                }
            });
        }
        return channels;
    }

    private List<String> getChannelNames(ChannelCategory channelCategory) {
        if (channelCategory == ChannelCategory.AD)
            return Arrays.asList("Food", "Retail", "Transport", "Gaming", "Movies", "Fire", "Accident");
        else if (channelCategory == ChannelCategory.PANIC)
            return Arrays.asList("Fire", "Accident", "Require Blood", "Ambulance", "Earthquake", "Cyclone");
        return Arrays.asList();
    }
}
