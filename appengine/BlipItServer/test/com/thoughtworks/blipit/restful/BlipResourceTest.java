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

package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.AbstractResourceTest;
import com.thoughtworks.blipit.TestData;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.restful.stubs.BlipResourceStub;
import com.thoughtworks.blipit.restful.stubs.BlipsResourceStub;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipResourceTest extends AbstractResourceTest {

    @Before
    public void setUp() throws Exception {
        dataStoreStub.makePersistent(TestData.AdChannels.MOVIE);
        Set<Key> movieChannelKeys = makeSet(TestData.AdChannels.MOVIE.getKey());
        dataStoreStub.makePersistent(TestData.AdBlips.NAVARANG.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.AdBlips.FAMELIDO.setChannelKeys(movieChannelKeys));
    }
    
    @Test
    public void shouldGetGivenBlip() throws IOException {
        Blip expectedBlip = TestData.AdBlips.FAMELIDO;
        String blipId = String.valueOf(expectedBlip.getKey().getId());
        BlipResourceStub blipResourceStub = new BlipResourceStub(blipId);

        assertBlip(blipResourceStub.performGet(), expectedBlip);
    }
    
    @Test
    public void shouldDeleteGivenBlip() throws Exception {
        BlipsResourceStub blipsResourceStub = new BlipsResourceStub("ad");
        int initialCountOfPanicBlips = blipsResourceStub.performGet().size();

        String blipId = String.valueOf(TestData.AdBlips.NAVARANG.getKey().getId());
        BlipResourceStub blipResourceStub = new BlipResourceStub(blipId);
        assertThat(blipResourceStub.performDelete(), is(true));

        int finalCountOfPanicBlips = blipsResourceStub.performGet().size();
        assertThat(finalCountOfPanicBlips, is(initialCountOfPanicBlips - 1));
    }
}
