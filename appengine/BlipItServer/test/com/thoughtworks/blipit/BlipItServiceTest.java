package com.thoughtworks.blipit;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItServiceTest {
    
    private BlipItSubscribeResourceImpl blipItSubscribeServerResource;

    @Before
    public void setup() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
    }
    
    @Test
    public void firstTest() {
        String message = blipItSubscribeServerResource.showMessage();
        assertThat(message.contains("HTTP"), is(true));
    }
}
