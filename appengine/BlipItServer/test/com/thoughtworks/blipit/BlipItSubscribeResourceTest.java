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

import com.thoughtworks.contract.common.Channel;
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

public class BlipItSubscribeResourceTest extends DataStoreStubTest {

    private BlipItSubscribeResource blipItSubscribeServerResource;

    @Override
    protected void doSetup() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
    }

    @Override
    protected void doTearDown() {  }


    @Test
    public void testShowMessage() {
        String message = blipItSubscribeServerResource.showMessage();
        assertThat(message.contains("HTTP"), is(true));
    }

    @Test
    public void testGetBlipsFiltersAlertsBasedOnUsersChannelPreference() {
        final DatastoreStub datastoreStub = new DatastoreStub();
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.FameMovie());
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.PVRMovie());
        datastoreStub.setupEntityAsPersisted(TestData.Alerts.MTRFood());

        final GetBlipsRequest blipItRequest = GetBlipItRequest(Arrays.asList(TestData.Channels.Movie()));
        final GetBlipsResponse blips = blipItSubscribeServerResource.getBlips(blipItRequest);

        assertThat(blips.isSuccess(), is(true));
        assertThat(blips.getBlips().size(), is(2));
    }

    private GetBlipsRequest GetBlipItRequest(List<Channel> channels) {
        final ArrayList<Channel> userChannels = new ArrayList<Channel>();
        userChannels.addAll(channels);
        final UserPrefs userPrefs = new UserPrefs();
        userPrefs.setChannels(userChannels);
        final GetBlipsRequest blipItRequest = new GetBlipsRequest();
        blipItRequest.setUserPrefs(userPrefs);
        return blipItRequest;
    }
}