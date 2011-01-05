package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.AbstractDataStoreStubTest;
import com.thoughtworks.blipit.TestData;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.restful.stubs.BlipsResourceStub;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class BlipsResourceTest extends AbstractDataStoreStubTest {

    @Before
    public void setUp() throws Exception {
        dataStoreStub.makePersistent(TestData.AdChannels.MOVIE);
        dataStoreStub.makePersistent(TestData.AdChannels.FOOD);
        dataStoreStub.makePersistent(TestData.PanicChannels.ACCIDENT);
        dataStoreStub.makePersistent(TestData.PanicChannels.EARTHQUAKE);
        dataStoreStub.makePersistent(TestData.PanicChannels.FIRE);

        Set<Key> movieChannelKeys = makeSet(TestData.AdChannels.MOVIE.getKey());
        Set<Key> foodChannelKeys = makeSet(TestData.AdChannels.FOOD.getKey());
        Set<Key> panicChannelKeys = makeSet(TestData.PanicChannels.ACCIDENT.getKey(), TestData.PanicChannels.FIRE.getKey());
        dataStoreStub.makePersistent(TestData.AdBlips.NAVARANG.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.AdBlips.FAMELIDO.setChannelKeys(movieChannelKeys));
        dataStoreStub.makePersistent(TestData.AdBlips.MTR.setChannelKeys(foodChannelKeys));
        dataStoreStub.makePersistent(TestData.PanicBlips.PANIC1.setChannelKeys(panicChannelKeys));
    }
    
    @Test
    public void shouldGetAllBlipsForPanicCategory() throws Exception {
        BlipsResourceStub blipsResourceStub = new BlipsResourceStub("panic");
        List<Blip> blips = blipsResourceStub.performGet();

        assertThat(blips, is(not(nullValue())));
        assertThat(blips.size(), is(1));
        assertBlip(blips.get(0), TestData.PanicBlips.PANIC1);
    }

    @Test
    public void shouldGetAllBlipsForAdCategory() throws Exception {
        BlipsResourceStub blipsResourceStub = new BlipsResourceStub("ad");
        List<Blip> blips = blipsResourceStub.performGet();

        assertThat(blips, is(not(nullValue())));
        assertThat(blips.size(), is(3));
        assertBlip(blips.get(0), TestData.AdBlips.NAVARANG);
        assertBlip(blips.get(1), TestData.AdBlips.FAMELIDO);
        assertBlip(blips.get(2), TestData.AdBlips.MTR);
    }

    private void assertBlip(Blip actualBlip, Blip expectedBlip) {
        assertThat(actualBlip.getTitle(), is(expectedBlip.getTitle()));
        assertThat(actualBlip.getDescription(), is(expectedBlip.getDescription()));
    }
}
