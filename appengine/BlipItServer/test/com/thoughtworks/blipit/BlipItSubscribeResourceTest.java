package com.thoughtworks.blipit;

import com.thoughtworks.contract.subscribe.BlipItSubscribeResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItSubscribeResourceTest {

    private BlipItSubscribeResource blipItSubscribeServerResource;

    @Before
    public void setup() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void showMessage() {
        String message = blipItSubscribeServerResource.showMessage();
        assertThat(message.contains("HTTP"), is(true));
    }

    //TODO : Finish this <Kiran>
    @Test
    public void blipsAreFilteredBasedOnUsersRadiusPreference() {

    }
}
