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

import com.thoughtworks.contract.BlipItSubscribeResource;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItSubscribeResourceTest {
    
    private BlipItSubscribeResource blipItSubscribeServerResource;

    @Before
    public void setup() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
        // insert test alerts here to the datastore
    }
    
    @Test
    public void testShowMessage() {
        String message = blipItSubscribeServerResource.showMessage();
        assertThat(message.contains("HTTP"), is(true));
    }
}
