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

package com.thoughtworks.blipit.services;

import android.os.Handler;
import android.os.Message;
import com.google.android.maps.GeoPoint;

import static com.thoughtworks.blipit.utils.BlipItUtils.MSG_REGISTER_CLIENT;
import static com.thoughtworks.blipit.utils.BlipItUtils.MSG_UNREGISTER_CLIENT;
import static com.thoughtworks.blipit.utils.BlipItUtils.MSG_USER_LOCATION_UPDATED;
import static com.thoughtworks.blipit.utils.BlipItUtils.getGeoPointFromBundle;

public class BlipNotificationServiceHandler extends Handler {
    private BlipNotificationService blipNotificationService;

    public BlipNotificationServiceHandler(BlipNotificationService blipNotificationService) {
        this.blipNotificationService = blipNotificationService;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_REGISTER_CLIENT:
                blipNotificationService.addClient(msg.replyTo);
                break;
            case MSG_UNREGISTER_CLIENT:
                blipNotificationService.removeClient(msg.replyTo);
                break;
            case MSG_USER_LOCATION_UPDATED:
                GeoPoint geoPoint = getGeoPointFromBundle(msg.getData());
                blipNotificationService.setCurrentUserLocation(geoPoint);
                break;
            default:
                super.handleMessage(msg);
        }
    }
}
