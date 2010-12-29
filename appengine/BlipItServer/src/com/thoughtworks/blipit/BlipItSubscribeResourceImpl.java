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

package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.common.Channel;
import com.thoughtworks.contract.common.GetChannelsResponse;
import com.thoughtworks.contract.subscribe.BlipItSubscribeResource;
import com.thoughtworks.contract.subscribe.GetBlipsRequest;
import com.thoughtworks.contract.subscribe.GetBlipsResponse;
import com.thoughtworks.contract.subscribe.UserPrefs;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlipItSubscribeResourceImpl extends BlipItCommonServerResource implements BlipItSubscribeResource {
    private static final Logger log = Logger.getLogger(BlipItSubscribeResourceImpl.class.getName());

    public BlipItSubscribeResourceImpl() {
        super();
    }

    @Post
    public GetBlipsResponse getBlips(GetBlipsRequest blipItRequest) {
        final GetBlipsResponse blipItResponse = new GetBlipsResponse();
        UserPrefs userPrefs = blipItRequest.getUserPrefs();
        if (isEmpty(userPrefs)) return blipItResponse;
        blipItRepository.filterAlertsByChannels(getChannelKeys(userPrefs), new Utils.ResultHandler<Blip>() {
            public void onSuccess(Blip alert) {
                blipItResponse.setSuccess();
                blipItResponse.addBlips(alert.toBlip());
            }

            public void onError(Throwable throwable) {
                log.log(Level.SEVERE, "An error occurred while fetching alerts", throwable);
                blipItResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
            }
        });
        return blipItResponse;
    }

    private Set<Key> getChannelKeys(UserPrefs userPrefs) {
        Set<Key> channelKeys = new HashSet<Key>();
        for (Channel channel : userPrefs.getChannels()) {
            channelKeys.add(KeyFactory.stringToKey(channel.getId()));
        }
        return channelKeys;
    }

    private boolean isEmpty(UserPrefs userPrefs) {
        return userPrefs == null || userPrefs.isEmpty();
    }

    @Get
    public GetChannelsResponse getAvailableChannels(Category channelCategory) {
        return getChannels(channelCategory);
    }

}
