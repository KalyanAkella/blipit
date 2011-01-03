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
import java.util.List;
import java.util.Set;

public class BlipItRepository {
    public void filterBlipsByChannels(final Set<Key> channelKeys, Utils.ResultHandler<Blip> handler) {
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

    public void retrieveChannelsByCategory(final Category channelCategory, final Utils.ResultHandler<Channel> handler) {
        DataStoreHelper.retrieveAllAndProcess(Channel.class, new Utils.QueryHandler() {
            public void prepare(Query query) {
                query.declareParameters("com.google.appengine.api.datastore.Category channelCategory");
                query.setFilter("this.category == channelCategory");
            }

            public Object[] parameters() {
                return new Object[] {channelCategory};
            }
        }, handler);
    }

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
}
