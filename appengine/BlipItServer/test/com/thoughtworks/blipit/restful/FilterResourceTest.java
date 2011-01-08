package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.AbstractResourceTest;
import com.thoughtworks.blipit.TestData;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.domain.Filter;
import com.thoughtworks.blipit.restful.stubs.FilterResourceStub;
import com.thoughtworks.blipit.restful.stubs.FiltersResourceStub;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class FilterResourceTest extends AbstractResourceTest {

    private Set<Key> movieChannelKeys;

    @Before
    public void setUp() throws Exception {
        dataStoreStub.makePersistent(TestData.AdChannels.MOVIE);
        dataStoreStub.makePersistent(TestData.AdChannels.FOOD);

        movieChannelKeys = makeSet(TestData.AdChannels.MOVIE.getKey());
        dataStoreStub.makePersistent(TestData.AdBlips.NAVARANG.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.AdBlips.FAMELIDO.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.AdBlips.MTR);
        dataStoreStub.makePersistent(TestData.PanicBlips.PANIC1);
        dataStoreStub.makePersistent(TestData.AdBlips.PVR.setChannelKeys(movieChannelKeys));
    }
    
    @Test
    public void shouldFilterBlipsBasedOnChannelPreference() throws Exception {
        FiltersResourceStub filtersResourceStub = new FiltersResourceStub("ad");
        Filter movieFilter = prepareAdFilterWithMovieChannels(Float.MAX_VALUE);
        Filter savedMovieFilter = filtersResourceStub.performPost(movieFilter);

        String filterId = String.valueOf(savedMovieFilter.getKey().getId());
        FilterResourceStub filterResourceStub = new FilterResourceStub(filterId);
        List<Blip> blips = filterResourceStub.performGet();

        assertThat(blips, is(not(nullValue())));
        assertThat(blips.size(), is(3));
        assertBlip(blips.get(0), TestData.AdBlips.NAVARANG);
        assertBlip(blips.get(1), TestData.AdBlips.FAMELIDO);
        assertBlip(blips.get(2), TestData.AdBlips.PVR);
    }
    
    @Test
    public void shouldFilterBlipsBasedOnRadiusPreference() throws Exception {
        FiltersResourceStub filtersResourceStub = new FiltersResourceStub("ad");
        Filter movieFilter = prepareAdFilterWithMovieChannels(10000);
        Filter savedMovieFilter = filtersResourceStub.performPost(movieFilter);

        String filterId = String.valueOf(savedMovieFilter.getKey().getId());
        FilterResourceStub filterResourceStub = new FilterResourceStub(filterId);
        List<Blip> blips = filterResourceStub.performGet();

        assertThat(blips.size(), is(2));
        assertBlip(blips.get(0), TestData.AdBlips.NAVARANG);
        assertBlip(blips.get(1), TestData.AdBlips.FAMELIDO);
    }

    @Test
    public void shouldDeleteGivenFilter() throws Exception {
        FiltersResourceStub filtersResourceStub = new FiltersResourceStub("ad");
        int initialCountOfFilters = filtersResourceStub.performGet().size();
        float radius = Float.MAX_VALUE;
        Filter movieFilter = prepareAdFilterWithMovieChannels(radius);
        Filter savedMovieFilter = filtersResourceStub.performPost(movieFilter);

        String filterId = String.valueOf(savedMovieFilter.getKey().getId());
        FilterResourceStub filterResourceStub = new FilterResourceStub(filterId);
        assertThat(filterResourceStub.performDelete(), is(true));

        int finalCountOfFilters = filtersResourceStub.performGet().size();
        assertThat(finalCountOfFilters, is(initialCountOfFilters));
    }

    private Filter prepareAdFilterWithMovieChannels(float radius) {
        Filter movieFilter = TestData.AdFilters.filter1;
        movieFilter.setChannelKeys(movieChannelKeys);
        movieFilter.setGeoPoint(TestData.Locations.K3sHome());
        movieFilter.setRadius(radius);
        return movieFilter;
    }
}
