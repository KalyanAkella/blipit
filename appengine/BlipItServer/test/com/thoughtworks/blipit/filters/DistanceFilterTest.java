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

package com.thoughtworks.blipit.filters;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.AbstractResourceTest;
import com.thoughtworks.blipit.domain.Blip;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DistanceFilterTest extends AbstractResourceTest {

    private static final int PREFERRED_DISTANCE_IN_METERS = 2000;
    public DistanceFilter distanceFilter;

    @Before
    public void setup() {
        GeoPt geoPt = new GeoPt(12.96755569542792f, 77.56370584124754f);
        distanceFilter = new DistanceFilter(geoPt, PREFERRED_DISTANCE_IN_METERS);
    }
    
    @Test
    public void testDoFilter() throws Exception {
        List<Blip> blips = new ArrayList<Blip>();
        blips.add(makeBlip("FarBlip", "Blip more than 2KM", new GeoPt(12.953649862192545f, 77.54233399981688f)));
        blips.add(makeBlip("NearBlip", "Blip less than 2KM", new GeoPt(12.956974786201874f, 77.54990855807493f)));
        blips.add(makeBlip("ExactBlip", "Blip at exact user location", new GeoPt(12.96755569542792f, 77.56370584124754f)));
        distanceFilter.doFilter(blips);
        assertThat(blips.size(), is(2));
    }

    private Blip makeBlip(String title, String description, GeoPt geoPoint) {
        Blip farBlip = new Blip(title, description, geoPoint);
        dataStoreStub.makePersistent(farBlip);
        return farBlip;
    }
}
