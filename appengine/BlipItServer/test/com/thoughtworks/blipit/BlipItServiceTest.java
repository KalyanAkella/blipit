package com.thoughtworks.blipit;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItServiceTest {
    
    private BlipItSubscribeServerResource blipItSubscribeServerResource;

    @Before
    public void setup() {
        blipItSubscribeServerResource = new BlipItSubscribeServerResource();
    }
    
    @Test
    public void firstTest() {
        String message = blipItSubscribeServerResource.showMessage();
        assertThat(message.contains("HTTP"), is(true));
    }
}
