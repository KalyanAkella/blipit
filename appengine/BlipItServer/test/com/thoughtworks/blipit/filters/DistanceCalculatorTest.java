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
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DistanceCalculatorTest {

    public DistanceCalculator distanceCalculator;

    @Before
    public void setup() {
        distanceCalculator = new DistanceCalculator();
    }
    
    @Test
    public void testComputeDistance1() throws Exception {
        GeoPt src = new GeoPt(12.960448f, 77.643723f);
        GeoPt dest = new GeoPt(12.960164f, 77.643417f);
        double distance = distanceCalculator.computeDistance1(src, dest);
        assertDistance(45.746, distance);
    }

    @Test
    public void testComputeDistance2() throws Exception {
        GeoPt src = new GeoPt(12.960448f, 77.643723f);
        GeoPt dest = new GeoPt(12.960164f, 77.643417f);
        assertDistance(45.661, distanceCalculator.computeDistance2(src, dest));
    }

    private void assertDistance(double expected, double actual) {
        BigDecimal bigDecimal = new BigDecimal(actual);
        assertThat(bigDecimal.round(new MathContext(5, RoundingMode.HALF_UP)).doubleValue(), is(expected));
    }
}
