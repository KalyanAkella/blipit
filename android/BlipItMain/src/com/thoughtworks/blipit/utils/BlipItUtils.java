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

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.types.Ad;
import com.thoughtworks.blipit.types.Channel;
import com.thoughtworks.blipit.types.Filter;
import com.thoughtworks.blipit.types.Key;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlipItUtils {
    public static final String USER_LOCATION_LATITUDE = "USER_LOCATION_LATITUDE";
    public static final String USER_LOCATION_LONGITUDE = "USER_LOCATION_LONGITUDE";
    public static final String ADS = "BLIPS";
    public static final int MSG_REGISTER_CLIENT = 1;
    // TODO: handle unregister in the lifecycle methods of BlipItActivity
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_USER_LOCATION_UPDATED = 3;
    public static final int MSG_BLIPS_UPDATED = 4;
    public static final String APP_TAG = "BlipItActivity";
    public static final String RADIUS_PREF_KEY = "radius_pref_key";
    public static final String CHANNEL_PREF_KEY = "channel_pref_key";
    public static final String AD_CHANNELS_KEY = "ad_channels_key";
    public static final String CREATOR_ID = "%s:%s";
    public static final int METRES_PER_KM = 1000;
    public static final int NOTIFICATION_INTERVAL = 5 * 1000;

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

    public static Message getMessageWithBlips(ArrayList<Ad> ads, int messageId) {
        Message message = Message.obtain(null, messageId);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ADS, ads);
        message.setData(bundle);
        return message;
    }

    public static OverlayItem getOverlayItem(Ad ad) {
        return new OverlayItem(asGeoPoint(ad.getGeoPoint()), ad.getTitle(), ad.getDescription());
    }

    private static GeoPoint asGeoPoint(com.thoughtworks.blipit.types.Location location) {
        int latitude = (int) (location.getLatitude() * 1E6);
        int longitude = (int) (location.getLongitude() * 1E6);
        return new GeoPoint(latitude, longitude);
    }

    public static com.thoughtworks.blipit.types.Location toLocation(GeoPoint currentUserLocation) {
        com.thoughtworks.blipit.types.Location userLocation = new com.thoughtworks.blipit.types.Location();
        if (currentUserLocation != null) {
            userLocation.setLatitude((float) (currentUserLocation.getLatitudeE6() * 1E-6));
            userLocation.setLongitude((float) (currentUserLocation.getLongitudeE6() * 1E-6));
        }
        return userLocation;
    }

    public static float getRadius(SharedPreferences sharedPreferences, String key) {
        try {
            return Float.valueOf(sharedPreferences.getString(key, "2"));
        } catch (Exception e) {
            return sharedPreferences.getFloat(key, 2f);
        }
    }

    public static String toFilterJson(Filter filter) {
        return new Gson().toJson(filter);
    }

    public static Filter toFilter(String filterJson) {
        return new Gson().fromJson(filterJson, Filter.class);
    }

    public static List<Ad> toAds(String adsJson) {
        Type listOfTokensType = new TypeToken<List<Ad>>() {}.getType();
        return new Gson().fromJson(adsJson, listOfTokensType);
    }

    public static List<Channel> toChannels(String channelsJson) {
        Type listOfTokensType = new TypeToken<List<Channel>>() {}.getType();
        return new Gson().fromJson(channelsJson, listOfTokensType);
    }

    public static String toChannelsJson(List<Channel> channels) {
        Type listOfChannelsType = new TypeToken<List<Channel>>() {}.getType();
        return new Gson().toJson(channels, listOfChannelsType);
    }

    public static Set<Key> toChannelKeys(String channelPrefStr) {
        Set<Key> channelKeys = new HashSet<Key>();
        if (channelPrefStr != null) {
            for (Channel channel : toChannels(channelPrefStr)) {
                channelKeys.add(channel.getKey());
            }
        }
        return channelKeys;
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

    public static ArrayList<Ad> asArrayList(List<Ad> ads) {
        if (ads instanceof ArrayList) return (ArrayList<Ad>) ads;
        return new ArrayList<Ad>(ads);
    }
}
