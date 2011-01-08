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
import com.thoughtworks.blipit.domain.Filter;
import com.thoughtworks.blipit.restful.stubs.FiltersResourceStub;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FiltersResourceTest extends AbstractResourceTest {
    private Set<Key> adChannelKeys;

    @Before
    public void setUp() throws Exception {
        dataStoreStub.makePersistent(TestData.AdChannels.MOVIE);
        dataStoreStub.makePersistent(TestData.AdChannels.FOOD);

        adChannelKeys = makeSet(TestData.AdChannels.MOVIE.getKey(), TestData.AdChannels.FOOD.getKey());
    }

    @Test
    public void shouldSaveFilter() throws Exception {
        FiltersResourceStub filtersResourceStub = new FiltersResourceStub("ad");
        Filter expectedFilter = TestData.AdFilters.filter1;
        expectedFilter.setChannelKeys(adChannelKeys);
        Filter savedFilter = filtersResourceStub.performPost(expectedFilter);

        assertFilter(savedFilter, expectedFilter);
    }

    private void assertFilter(Filter actualFilter, Filter expectedFilter) {
        assertThat(actualFilter, is(not(nullValue())));
        assertThat(actualFilter.getKey(), is(not(nullValue())));
        assertThat(actualFilter.getRadius(), is(expectedFilter.getRadius()));
        assertThat(actualFilter.getGeoPoint(), is(expectedFilter.getGeoPoint()));
    }

}
