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

package com.thoughtworks.blipit.utils;

import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.thoughtworks.contract.Blip;

import java.util.ArrayList;

public class BlipItUtils {
    public static final String USER_LOCATION_LATITUDE = "USER_LOCATION_LATITUDE";
    public static final String USER_LOCATION_LONGITUDE = "USER_LOCATION_LONGITUDE";
    public static final String BLIPS = "BLIPS";
    public static final int MSG_REGISTER_CLIENT = 1;
    // TODO: handle unregister in the lifecycle methods of BlipItActivity
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_USER_LOCATION_UPDATED = 3;
    public static final int MSG_BLIPS_UPDATED = 4;

    private BlipItUtils() {
    }

    public static GeoPoint getGeoPointFromBundle(Bundle bundle) {
        if (!containsGeoPoint(bundle)) return null;
        int latitude = bundle.getInt(USER_LOCATION_LATITUDE);
        int longitude = bundle.getInt(USER_LOCATION_LONGITUDE);
        return new GeoPoint(latitude, longitude);
    }

    public static boolean containsGeoPoint(Bundle bundle) {
        return bundle != null && bundle.containsKey(USER_LOCATION_LATITUDE) && bundle.containsKey(USER_LOCATION_LONGITUDE);
    }

    public static void saveGeoPointInBundle(Bundle bundle, GeoPoint geoPoint) {
        bundle.putInt(USER_LOCATION_LATITUDE, geoPoint.getLatitudeE6());
        bundle.putInt(USER_LOCATION_LONGITUDE, geoPoint.getLongitudeE6());
    }

    public static Message getMessageWithGeoPoint(GeoPoint geoPoint, int messageId) {
        Message message = Message.obtain(null, messageId);
        Bundle bundle = new Bundle();
        bundle.putInt(USER_LOCATION_LATITUDE, geoPoint.getLatitudeE6());
        bundle.putInt(USER_LOCATION_LONGITUDE, geoPoint.getLongitudeE6());
        message.setData(bundle);
        return message;
    }

    public static GeoPoint asGeoPoint(Location location) {
        int latitude = (int) (location.getLatitude() * 1E6);
        int longitude = (int) (location.getLongitude() * 1E6);
        return new GeoPoint(latitude, longitude);
    }

    public static Message getMessageWithBlips(ArrayList<Blip> blips, int messageId) {
        Message message = Message.obtain(null, messageId);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BLIPS, blips);
        message.setData(bundle);
        return message;
    }

    public static GeoPoint getGeoPoint(Blip blip) {
        int latitudeE6 = blip.getBlipLocation().getLatitudeE6();
        int longitudeE6 = blip.getBlipLocation().getLongitudeE6();
        return new GeoPoint(latitudeE6, longitudeE6);
    }

    public static OverlayItem getOverlayItem(Blip blip) {
        GeoPoint geoPoint = getGeoPoint(blip);
        return new OverlayItem(geoPoint, blip.getTitle(), blip.getMessage());
    }
}
