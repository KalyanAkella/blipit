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

    private Set<Key> movieChannelKeys;
    private Set<Key> panicChannelKeys;

    @Before
    public void setUp() throws Exception {
        dataStoreStub.makePersistent(TestData.AdChannels.MOVIE);
        dataStoreStub.makePersistent(TestData.AdChannels.FOOD);
        dataStoreStub.makePersistent(TestData.PanicChannels.ACCIDENT);
        dataStoreStub.makePersistent(TestData.PanicChannels.EARTHQUAKE);
        dataStoreStub.makePersistent(TestData.PanicChannels.FIRE);

        movieChannelKeys = makeSet(TestData.AdChannels.MOVIE.getKey());
        Set<Key> foodChannelKeys = makeSet(TestData.AdChannels.FOOD.getKey());
        panicChannelKeys = makeSet(TestData.PanicChannels.ACCIDENT.getKey(), TestData.PanicChannels.FIRE.getKey());
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

    @Test
    public void shouldSaveAdBlip() throws Exception {
        BlipsResourceStub blipsResourceStub = new BlipsResourceStub("ad");
        Blip pvrBlip = TestData.AdBlips.PVR.setChannelKeys(movieChannelKeys);
        Blip savedBlip = blipsResourceStub.performPost(pvrBlip);

        assertBlip(savedBlip, pvrBlip);
    }

    @Test
    public void shouldSavePanicBlip() throws Exception {
        BlipsResourceStub blipsResourceStub = new BlipsResourceStub("panic");
        Blip panicBlip = TestData.PanicBlips.PANIC2.setChannelKeys(panicChannelKeys);
        Blip savedBlip = blipsResourceStub.performPost(panicBlip);

        assertBlip(savedBlip, panicBlip);
    }

    @Test
    public void shouldSavePanicBlipOnlyOncePerCreator() throws Exception {
        BlipsResourceStub blipsResourceStub = new BlipsResourceStub("panic");
        int initialCountOfPanicBlips = blipsResourceStub.performGet().size();

        Blip panicBlip = TestData.PanicBlips.PANIC2;
        panicBlip.setCreatorId("Creator");
        panicBlip.setChannelKeys(panicChannelKeys);

        Blip savedBlip = blipsResourceStub.performPost(panicBlip);
        assertBlip(savedBlip, panicBlip);

        panicBlip.setTitle("New Panic Title");
        savedBlip = blipsResourceStub.performPost(panicBlip);
        assertBlip(savedBlip, panicBlip);

        panicBlip.setDescription("New Panic Desc");
        savedBlip = blipsResourceStub.performPost(panicBlip);
        assertBlip(savedBlip, panicBlip);

        int finalCountOfPanicBlips = blipsResourceStub.performGet().size();
        assertThat(finalCountOfPanicBlips, is(initialCountOfPanicBlips + 1));
    }

}
