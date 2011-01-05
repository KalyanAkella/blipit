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

import android.os.Bundle;
import android.os.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.panicblip.types.Channel;
import com.thoughtworks.blipit.panicblip.types.Location;
import com.thoughtworks.blipit.panicblip.types.Panic;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    public static final String CREATOR_ID = "%s:%s";

    public static Location getLocation(android.location.Location lastKnownLocation) {
        com.thoughtworks.blipit.panicblip.types.Location geoLocation = new com.thoughtworks.blipit.panicblip.types.Location();
        geoLocation.setLatitude((float) lastKnownLocation.getLatitude());
        geoLocation.setLongitude((float) lastKnownLocation.getLongitude());
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

    public static List<String> toChannelNames(List<Channel> channels) {
        List<String> channelNames = new ArrayList<String>();
        if (channels != null) {
            for (Channel channel : channels) {
                channelNames.add(channel.getName());
            }
        }
        return channelNames;
    }

    public static List<Channel> toChannels(String channelsJson) {
        Type listOfTokensType = new TypeToken<List<Channel>>() {}.getType();
        return new Gson().fromJson(channelsJson, listOfTokensType);
    }

    public static String toJson(Panic panic) {
        return new Gson().toJson(panic);
    }

    public static Panic toPanic(String panicJson) {
        return new Gson().fromJson(panicJson, Panic.class);
    }

}
