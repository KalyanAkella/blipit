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
import com.thoughtworks.blipit.domain.Filter;

import javax.jdo.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlipItRepository {

    public List<Channel> retrieveChannelsByCategory(final Category channelCategory) {
        return DataStoreHelper.retrieveAll(Channel.class, new Utils.QueryHandler() {
            public void prepare(Query query) {
                query.declareParameters("com.google.appengine.api.datastore.Category channelCategory");
                query.setFilter("this.category == channelCategory");
            }

            public Object[] parameters() {
                return new Object[] {channelCategory};
            }
        });
    }

    public List<Blip> retrieveBlipsByCategory(Category blipCategory) {
        final Set<Key> channelKeys = getChannelKeys(blipCategory);
        return DataStoreHelper.retrieveAll(Blip.class, getQueryHandlerForChannels(channelKeys));
    }

    public List<Blip> retrieveBlipsByChannels(final Set<Key> channelKeys) {
        return DataStoreHelper.retrieveAll(Blip.class, getQueryHandlerForChannels(channelKeys));
    }

    private Utils.QueryHandler getQueryHandlerForChannels(final Set<Key> channelKeys) {
        return new Utils.QueryHandler() {
            public void prepare(Query query) {
                query.declareParameters("java.util.Set channelKeys");
                query.setFilter("channelKeys.contains(this.channelKeys)");
            }

            public Object[] parameters() {
                return new Object[]{channelKeys};
            }
        };
    }

    public List<Filter> retrieveFiltersByCategory(Category blipCategory) {
        final Set<Key> channelKeys = getChannelKeys(blipCategory);
        return DataStoreHelper.retrieveAll(Filter.class, getQueryHandlerForChannels(channelKeys));
    }

    private Set<Key> getChannelKeys(Category blipCategory) {
        List<Channel> channels = retrieveChannelsByCategory(blipCategory);
        final Set<Key> channelKeys = new HashSet<Key>();
        for (Channel channel : channels) {
            channelKeys.add(channel.getKey());
        }
        return channelKeys;
    }
}
