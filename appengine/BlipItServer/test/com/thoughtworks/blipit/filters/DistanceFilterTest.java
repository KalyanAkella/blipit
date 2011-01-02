package com.thoughtworks.blipit.filters;

import com.google.appengine.api.datastore.GeoPt;
import com.thoughtworks.blipit.AbstractDataStoreStubTest;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.contract.GeoLocation;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DistanceFilterTest extends AbstractDataStoreStubTest {

    private static final int PREFERRED_DISTANCE_IN_METERS = 2000;
    public DistanceFilter distanceFilter;

    @Before
    public void setup() {
        GeoLocation geoLocation = new GeoLocation();
        geoLocation.setLatitude(12.96755569542792);
        geoLocation.setLongitude(77.56370584124754);
        distanceFilter = new DistanceFilter(geoLocation, PREFERRED_DISTANCE_IN_METERS);
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
