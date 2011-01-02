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
