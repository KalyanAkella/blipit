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

import java.util.ArrayList;
import java.util.Arrays;

public class PanicBlipUtils {
    public static final int REPORT_PANIC = 0;
    public static final String APP_TAG = "PanicBlipActivity";
    public static final String PANIC_BLIP = "PANIC_BLIP";
    public static final int CLEAR_PANIC = 1;
    public static final int LOCATION_CHANGED = 2;

    public static GeoLocation getGeoLocation(Location lastKnownLocation) {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(lastKnownLocation.getLatitude());
        geoLocation.setLongitude(lastKnownLocation.getLongitude());
        return geoLocation;
    }

    public static Message getMessageWithPanicTopics(int messageId, String... topics) {
        Message message = Message.obtain(null, messageId);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(PANIC_BLIP, asArrayList(topics));
        message.setData(bundle);
        return message;
    }

    public static ArrayList<String> asArrayList(String... strs) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(Arrays.asList(strs));
        return arrayList;
    }
}
