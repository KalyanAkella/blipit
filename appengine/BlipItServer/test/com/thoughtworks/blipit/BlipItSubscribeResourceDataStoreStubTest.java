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
import com.thoughtworks.blipit.domain.Alert;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.contract.GeoLocation;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.subscribe.Blip;
import com.thoughtworks.contract.subscribe.BlipItSubscribeResource;
import com.thoughtworks.contract.subscribe.GetBlipsRequest;
import com.thoughtworks.contract.subscribe.GetBlipsResponse;
import com.thoughtworks.contract.subscribe.UserPrefs;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItSubscribeResourceDataStoreStubTest extends DataStoreStubTest {

    private BlipItSubscribeResource blipItSubscribeServerResource;

    private final float MAXIMUM_RADIUS = 10000f; //10km
    private final GeoPt userLocation = TestData.Locations.K3sHome();

    @Override
    protected void doSetup() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
    }

    @Override
    protected void doTearDown() {
    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersChannelPreference() {
        final DatastoreStub datastoreStub = new DatastoreStub();
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.NavarangTheatre());
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.FameLido());
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.MTR());

        final GetBlipsRequest blipItRequest = constructGetBlipsRequest(Arrays.asList(TestData.Channels.Movie()), MAXIMUM_RADIUS);
        final GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);

        assertThat(blips.isSuccess(), is(true));
        assertThat(blips.getBlips().size(), is(2));
        assertBlip(blips.getBlips().get(0), TestData.Alerts.NavarangTheatre());
        assertBlip(blips.getBlips().get(1), TestData.Alerts.FameLido());
    }

    private void assertBlip(Blip blip, Alert alert) {
        assertThat(blip.getTitle(), is(alert.getTitle()));
        assertThat(blip.getMessage(), is(alert.getDescription()));
        assertThat(GeoLocationToGeoPointConverter.Convert(blip.getBlipLocation()), is(alert.getGeoPoint()));
    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersDistanceOfConvinience() {
        final DatastoreStub datastoreStub = new DatastoreStub();
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.NavarangTheatre());
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.PVRCinemas());
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.FameLido());

        final GetBlipsRequest blipItRequest = constructGetBlipsRequest(Arrays.asList(TestData.Channels.Movie()), MAXIMUM_RADIUS);
        final GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);

        assertThat(blips.isSuccess(), is(true));
        assertThat(blips.getBlips().size(), is(2));
        assertBlip(blips.getBlips().get(0), TestData.Alerts.NavarangTheatre());
        assertBlip(blips.getBlips().get(1), TestData.Alerts.FameLido());
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
            String description = userChannel.getDescription();
            com.thoughtworks.contract.common.Channel channel = new com.thoughtworks.contract.common.Channel(id, name, description, Category.AD);
            channels.add(channel);
        }
        return channels;
    }
}