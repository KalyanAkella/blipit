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

import com.thoughtworks.blipit.restful.BlipResource;
import com.thoughtworks.blipit.restful.BlipsResource;
import com.thoughtworks.blipit.restful.ChannelResource;
import com.thoughtworks.blipit.restful.ChannelsResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BlipItService extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/subscribe", BlipItSubscribeResourceImpl.class);
        router.attach("/{category}/channel", ChannelsResource.class);
        router.attach("/{category}/channel/{channel_id}", ChannelResource.class);
        router.attach("/{category}/blip", BlipsResource.class);
        router.attach("/{category}/blip/{blip_id}", BlipResource.class);
        return router;
    }
}
