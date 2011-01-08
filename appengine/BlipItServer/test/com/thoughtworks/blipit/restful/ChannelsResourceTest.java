package com.thoughtworks.blipit.restful;

import com.thoughtworks.blipit.AbstractResourceTest;
import com.thoughtworks.blipit.TestData;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.restful.stubs.ChannelsResourceStub;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class ChannelsResourceTest extends AbstractResourceTest {
    @Before
    public void setUp() throws Exception {
        dataStoreStub.makePersistent(TestData.AdChannels.MOVIE);
        dataStoreStub.makePersistent(TestData.AdChannels.FOOD);
        dataStoreStub.makePersistent(TestData.PanicChannels.ACCIDENT);
        dataStoreStub.makePersistent(TestData.PanicChannels.EARTHQUAKE);
        dataStoreStub.makePersistent(TestData.PanicChannels.FIRE);
    }
    
    @Test
    public void shouldGetAllChannelsForAdCategory() throws Exception {
        ChannelsResourceStub channelsResourceStub = new ChannelsResourceStub("ad");
        List<Channel> channels = channelsResourceStub.performGet();

        assertThat(channels, is(not(nullValue())));
        assertThat(channels.size(), is(2));
        assertChannel(channels.get(0), TestData.AdChannels.MOVIE);
        assertChannel(channels.get(1), TestData.AdChannels.FOOD);
    }

    @Test
    public void shouldGetAllChannelsForPanicCategory() throws Exception {
        ChannelsResourceStub channelsResourceStub = new ChannelsResourceStub("panic");
        List<Channel> channels = channelsResourceStub.performGet();

        assertThat(channels, is(not(nullValue())));
        assertThat(channels.size(), is(3));
        assertChannel(channels.get(0), TestData.PanicChannels.ACCIDENT);
        assertChannel(channels.get(1), TestData.PanicChannels.EARTHQUAKE);
        assertChannel(channels.get(2), TestData.PanicChannels.FIRE);
    }

    private void assertChannel(Channel actualChannel, Channel expectedChannel) {
        assertThat(actualChannel, is(not(nullValue())));
        assertThat(actualChannel.getName(), is(expectedChannel.getName()));
        assertThat(actualChannel.getCategory().getCategory(), is(expectedChannel.getCategory().getCategory()));
    }
}
