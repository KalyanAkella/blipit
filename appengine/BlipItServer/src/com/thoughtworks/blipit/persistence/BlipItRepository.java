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

package com.thoughtworks.blipit.persistence;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.domain.Channel;

import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class BlipItRepository {
    public void filterAlertsByChannels(final Set<Key> channelKeys, Utils.ResultHandler<Blip> handler) {
        DataStoreHelper.retrieveAllAndProcess(
                Blip.class,
                new Utils.QueryHandler() {
                    public void prepare(Query query) {
                        query.declareParameters("java.util.Set channelKeys");
                        query.setFilter("channelKeys.contains(this.channelKeys)");
//                        query.declareVariables("com.google.appengine.api.datastore.Key channelKey");
//                        query.setFilter("this.channelKeys.contains(channelKey) && channelKeys.contains(channelKey)");
                    }

                    public Object[] parameters() {
                        return new Object[]{channelKeys};
                    }
                },
                handler);
    }

    public void retrieveChannelsByCategory(Category channelCategory, Utils.ResultHandler<Channel> handler) {
        List<Channel> channels = saveAndGetChannels(channelCategory, handler);
        for (Channel channel : channels) {
            handler.onSuccess(channel);
        }
    }

    // TODO: Currently saves & gets the channels. This should go away in future. Find out a way to insert reference data like Channels.
    private List<Channel> saveAndGetChannels(Category channelCategory, final Utils.ResultHandler<Channel> handler) {
        List<String> channelNames = getChannelNames(channelCategory);
        final List<Channel> channels = new ArrayList<Channel>();
        for (String channelName : channelNames) {
            final Channel channel = new Channel();
            channel.setCategory(channelCategory);
            channel.setName(channelName);
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

    private List<String> getChannelNames(Category channelCategory) {
        if (com.thoughtworks.contract.common.Category.AD.name().equals(channelCategory.getCategory()))
            return Arrays.asList("Food", "Retail", "Transport", "Gaming", "Movies", "Fire", "Accident");
        else if (com.thoughtworks.contract.common.Category.PANIC.name().equals(channelCategory.getCategory()))
            return Arrays.asList("Fire", "Accident", "Require Blood", "Ambulance", "Earthquake", "Cyclone");
        return Arrays.asList();
    }
}