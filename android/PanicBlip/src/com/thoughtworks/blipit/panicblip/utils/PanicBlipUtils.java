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

package com.thoughtworks.blipit.panicblip.utils;

import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import com.thoughtworks.contract.GeoLocation;

import java.io.Serializable;

public class PanicBlipUtils {
    public static final int MSG_REGISTER_CLIENT = 0;
    public static final int REPORT_PANIC = 1;
    public static final int REPORT_PANIC_SUCCESS = 2;
    public static final int REPORT_PANIC_FAILURE = 3;
    public static final int REPORT_PANIC_LOC_ERROR = 4;
    public static final int CLEAR_PANIC = 5;
    public static final int CLEAR_PANIC_SUCCESS = 6;
    public static final int CLEAR_PANIC_FAILURE = 7;
    public static final int LOCATION_CHANGED = 8;
    public static final int MSG_UNREGISTER_CLIENT = 9;
    public static final String APP_TAG = "PanicBlipActivity";
    public static final String PANIC_CHANNELS_KEY = "panic_channels_key";
    public static final String PANIC_DATA = "panic_data";

    public static GeoLocation getGeoLocation(Location lastKnownLocation) {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(lastKnownLocation.getLatitude());
        geoLocation.setLongitude(lastKnownLocation.getLongitude());
        return geoLocation;
    }

    public static boolean areSameStrings(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static Message getMessage(int messageId, Serializable data) {
        Message message = Message.obtain(null, messageId);
        if (data != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(PANIC_DATA, data);
            message.setData(bundle);
        }
        return message;
    }
}
