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
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.subscribe.BlipItSubscribeResource;
import com.thoughtworks.contract.subscribe.GetBlipsRequest;
import com.thoughtworks.contract.subscribe.GetBlipsResponse;
import com.thoughtworks.contract.subscribe.UserPrefs;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItSubscribeResourceDataStoreStubTest extends DataStoreStubTest {

    private BlipItSubscribeResource blipItSubscribeServerResource;

    private final float MAXIMUM_RADIUS = 10000f; //10km
    private final GeoPt userLocation = TestData.Locations.K3sHome();

    @Before
    public void setUp() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
        DataStoreStub dataStoreStub = new DataStoreStub();
        TestData.Channels.MOVIE.setKey(dataStoreStub.setupEntityAsPersisted(TestData.Channels.MOVIE));
        TestData.Channels.FOOD.setKey(dataStoreStub.setupEntityAsPersisted(TestData.Channels.FOOD));

        Set<Key> movieChannelKeys = makeSet(TestData.Channels.MOVIE.getKey());
        Set<Key> foodChannelKeys = makeSet(TestData.Channels.FOOD.getKey());
        TestData.Alerts.NAVARANG.setKey(dataStoreStub.setupEntityAsPersisted(TestData.Alerts.NAVARANG.setChannelKeys(movieChannelKeys)));
        TestData.Alerts.FAMELIDO.setKey(dataStoreStub.setupEntityAsPersisted(TestData.Alerts.FAMELIDO.setChannelKeys(movieChannelKeys)));
        TestData.Alerts.MTR.setKey(dataStoreStub.setupEntityAsPersisted(TestData.Alerts.MTR.setChannelKeys(foodChannelKeys)));
        TestData.Alerts.PVR.setKey(dataStoreStub.setupEntityAsPersisted(TestData.Alerts.PVR.setChannelKeys(movieChannelKeys)));
    }

    private Set<Key> makeSet(Key... keys) {
        Set<Key> keySet = new HashSet<Key>();
        keySet.addAll(Arrays.asList(keys));
        return keySet;
    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersChannelPreference() {
        GetBlipsRequest blipItRequest = constructGetBlipsRequest(Arrays.asList(TestData.Channels.MOVIE), MAXIMUM_RADIUS);
        GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);

        assertThat(blips.isSuccess(), is(true));
        assertThat(blips.getBlips().size(), is(3));
        assertBlip(blips.getBlips().get(0), TestData.Alerts.NAVARANG);
        assertBlip(blips.getBlips().get(1), TestData.Alerts.FAMELIDO);
        assertBlip(blips.getBlips().get(2), TestData.Alerts.PVR);
    }

    private void assertBlip(com.thoughtworks.contract.subscribe.Blip blip, Blip alert) {
        assertThat(blip.getTitle(), is(alert.getTitle()));
        assertThat(blip.getMessage(), is(alert.getDescription()));
        assertThat(GeoLocationToGeoPointConverter.Convert(blip.getBlipLocation()), is(alert.getGeoPoint()));
    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersDistanceOfConvinience() {
        GetBlipsRequest blipItRequest = constructGetBlipsRequest(Arrays.asList(TestData.Channels.MOVIE), MAXIMUM_RADIUS);
        GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);

        assertThat(blips.isSuccess(), is(true));
        assertThat(blips.getBlips().size(), is(3));
        assertBlip(blips.getBlips().get(0), TestData.Alerts.NAVARANG);
        assertBlip(blips.getBlips().get(1), TestData.Alerts.FAMELIDO);
        assertBlip(blips.getBlips().get(2), TestData.Alerts.PVR);
    }

    private GetBlipsRequest constructGetBlipsRequest(List<Channel> channels, float distanceOfConvenience) {
        final ArrayList<Channel> userChannels = new ArrayList<Channel>();
        userChannels.addAll(channels);
        final UserPrefs userPrefs = new UserPrefs();
        userPrefs.setChannels(getChannels(userChannels));
        userPrefs.setRadius(distanceOfConvenience);

        final GeoLocation userLocation = new GeoLocation();
        userLocation.setLatitude(this.userLocation.getLatitude());
        userLocation.setLongitude(this.userLocation.getLongitude());

        final GetBlipsRequest blipItRequest = new GetBlipsRequest();
        blipItRequest.setUserPrefs(userPrefs);
        blipItRequest.setUserLocation(userLocation);
        return blipItRequest;
    }

    private List<com.thoughtworks.contract.common.Channel> getChannels(List<Channel> userChannels) {
        ArrayList<com.thoughtworks.contract.common.Channel> channels = new ArrayList<com.thoughtworks.contract.common.Channel>();
        for (Channel userChannel : userChannels) {
            String id = userChannel.getKeyAsString();
            String name = userChannel.getName();
            com.thoughtworks.contract.common.Channel channel = new com.thoughtworks.contract.common.Channel(id, name, Category.AD);
            channels.add(channel);
        }
        return channels;
    }
}