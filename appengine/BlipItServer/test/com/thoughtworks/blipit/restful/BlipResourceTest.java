package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.AbstractDataStoreStubTest;
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

public class BlipResourceTest extends AbstractDataStoreStubTest {

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
