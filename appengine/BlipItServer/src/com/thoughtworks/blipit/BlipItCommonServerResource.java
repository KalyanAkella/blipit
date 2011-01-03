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

import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.BlipItRepository;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.common.GetChannelsResponse;
import org.restlet.resource.ServerResource;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlipItCommonServerResource extends ServerResource {
    private static final Logger log = Logger.getLogger(BlipItCommonServerResource.class.getName());
    protected BlipItRepository blipItRepository;

    public BlipItCommonServerResource() {
        blipItRepository = new BlipItRepository();
    }

    public GetChannelsResponse getChannels(final Category channelCategory) {
        final GetChannelsResponse channelsResponse = new GetChannelsResponse();
        blipItRepository.retrieveChannelsByCategory(Utils.convert(channelCategory), new Utils.ResultHandler<Channel>() {
            public void onSuccess(Channel savedChannel) {
                channelsResponse.setSuccess();
                channelsResponse.addChannel(savedChannel.getKeyAsString(), savedChannel.getName(), channelCategory);
            }

            public void onError(Throwable throwable) {
                log.log(Level.SEVERE, "An error occurred while fetching channels", throwable);
                channelsResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
            }
        });
        return channelsResponse;
    }
}
