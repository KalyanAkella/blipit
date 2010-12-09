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
import com.thoughtworks.blipit.persistance.DataStoreHelper;
import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;
import com.thoughtworks.contract.BlipItSubscribeResource;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.logging.Logger;

public class BlipItSubscribeResourceImpl extends ServerResource implements BlipItSubscribeResource {
    private static final Logger log = Logger.getLogger(BlipItSubscribeResourceImpl.class.getName());

    @Get
    public String showMessage() {
        return "Hi there ! You've reached the BlipIt server !! I can only process HTTP post requests !!!";
    }

    @Post
    public BlipItResponse getBlips(BlipItRequest blipItRequest) {
        final BlipItResponse blipItResponse = new BlipItResponse();
        DataStoreHelper.retrieveAllAndProcess(Alert.class,
                new Utils.Task<Alert>() {
                    public void perform(Alert arg) {
                        blipItResponse.addBlips(arg.toBlip());
                    }
                }, new Utils.Task<Throwable>() {
                    public void perform(Throwable arg) {
                        blipItResponse.setBlipItError(Utils.getBlipItError(arg.getMessage()));
                    }
                });
        return blipItResponse;
    }
}
