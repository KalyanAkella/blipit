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

import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.persistance.AlertRepository;
import com.thoughtworks.contract.subscribe.GetBlipsRequest;
import com.thoughtworks.contract.subscribe.GetBlipsResponse;
import com.thoughtworks.contract.subscribe.BlipItSubscribeResource;
import com.thoughtworks.contract.subscribe.UserPrefs;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.logging.Level;
import java.util.logging.Logger;

public class BlipItSubscribeResourceImpl extends ServerResource implements BlipItSubscribeResource {
    private static final Logger log = Logger.getLogger(BlipItSubscribeResourceImpl.class.getName());
    private AlertRepository alertRepository;

    public BlipItSubscribeResourceImpl() {
        alertRepository = new AlertRepository();
    }

    @Get
    public String showMessage() {
        return "Hi there ! You've reached the BlipIt server !! I can only process HTTP post requests !!!";
    }

    @Post
    public GetBlipsResponse getBlips(GetBlipsRequest blipItRequest) {
        final GetBlipsResponse blipItResponse = new GetBlipsResponse();
        UserPrefs userPrefs = blipItRequest.getUserPrefs();
        if (userPrefs == null || userPrefs.getChannels() == null || userPrefs.getChannels().isEmpty()) return blipItResponse;
        alertRepository.filterAlerts(blipItRequest.getUserLocation(), userPrefs, new Utils.ResultHandler<Alert>() {
            public void handle(Alert alert) {
                blipItResponse.addBlips(alert.toBlip());
            }

            public void onError(Throwable throwable) {
                log.log(Level.SEVERE, "An error occurred while fetching alerts", throwable);
                blipItResponse.setBlipItError(Utils.getBlipItError(throwable.getMessage()));
            }
        });
        return blipItResponse;
    }
}
