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

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class FiltersResourceStubTest extends AbstractDataStoreStubTest {
    private final float MAXIMUM_RADIUS = 10000f; //10km
    private final GeoPt userLocation = TestData.Locations.K3sHome();

    @Before
    public void setUp() {
        dataStoreStub.makePersistent(TestData.Channels.MOVIE);
        dataStoreStub.makePersistent(TestData.Channels.FOOD);

        Set<Key> movieChannelKeys = makeSet(TestData.Channels.MOVIE.getKey());
        Set<Key> foodChannelKeys = makeSet(TestData.Channels.FOOD.getKey());
        dataStoreStub.makePersistent(TestData.Blips.NAVARANG.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.Blips.FAMELIDO.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.Blips.MTR.setChannelKeys(foodChannelKeys));
        dataStoreStub.makePersistent(TestData.Blips.PVR.setChannelKeys(movieChannelKeys));
    }

    private Set<Key> makeSet(Key... keys) {
        Set<Key> keySet = new HashSet<Key>();
        keySet.addAll(Arrays.asList(keys));
        return keySet;
    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersChannelPreference() {
//
//
//
//        GetBlipsRequest blipItRequest = constructGetBlipsRequest(Arrays.asList(TestData.Channels.MOVIE), MAXIMUM_RADIUS);
//        GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);
//
//        assertThat(blips.isSuccess(), is(true));
//        assertThat(blips.getBlips().size(), is(2));
//        assertBlip(blips.getBlips().get(0), TestData.Blips.NAVARANG);
//        assertBlip(blips.getBlips().get(1), TestData.Blips.FAMELIDO);
    }

//    private void assertBlip(com.thoughtworks.contract.subscribe.Blip blip, Blip alert) {
//        assertThat(blip.getTitle(), is(alert.getTitle()));
//        assertThat(blip.getMessage(), is(alert.getDescription()));
//        assertThat(Utils.asGeoPoint(blip.getBlipLocation()), is(alert.getGeoPoint()));
//    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersDistanceOfConvinience() {
//        GetBlipsRequest blipItRequest = constructGetBlipsRequest(Arrays.asList(TestData.Channels.MOVIE), MAXIMUM_RADIUS);
//        GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);
//
//        assertThat(blips.isSuccess(), is(true));
//        assertThat(blips.getBlips().size(), is(2));
//        assertBlip(blips.getBlips().get(0), TestData.Blips.NAVARANG);
//        assertBlip(blips.getBlips().get(1), TestData.Blips.FAMELIDO);
    }

//    private GetBlipsRequest constructGetBlipsRequest(List<Channel> channels, float distanceOfConvenience) {
//        final ArrayList<Channel> userChannels = new ArrayList<Channel>();
//        userChannels.addAll(channels);
//        final UserPrefs userPrefs = new UserPrefs();
//        userPrefs.setChannels(getChannels(userChannels));
//        userPrefs.setRadius(distanceOfConvenience);
//
//        final GeoLocation userLocation = new GeoLocation();
//        userLocation.setLatitude(this.userLocation.getLatitude());
//        userLocation.setLongitude(this.userLocation.getLongitude());
//
//        final GetBlipsRequest blipItRequest = new GetBlipsRequest();
//        blipItRequest.setUserPrefs(userPrefs);
//        blipItRequest.setUserLocation(userLocation);
//        return blipItRequest;
//    }

//    private List<com.thoughtworks.contract.common.Channel> getChannels(List<Channel> userChannels) {
//        ArrayList<com.thoughtworks.contract.common.Channel> channels = new ArrayList<com.thoughtworks.contract.common.Channel>();
//        for (Channel userChannel : userChannels) {
//            String id = userChannel.getKeyAsString();
//            String name = userChannel.getName();
//            com.thoughtworks.contract.common.Channel channel = new com.thoughtworks.contract.common.Channel(id, name, Category.AD);
//            channels.add(channel);
//        }
//        return channels;
//    }
}