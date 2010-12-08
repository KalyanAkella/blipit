package com.thoughtworks.blipit;

import com.thoughtworks.contract.BlipItSubscribeResource;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BlipItSubscribeResourceTest {
    
    private BlipItSubscribeResource blipItSubscribeServerResource;

    @Before
    public void setup() {
        blipItSubscribeServerResource = new BlipItSubscribeResourceImpl();
        // insert test alerts here to the datastore
    }
    
    @Test
    public void testShowMessage() {
        String message = blipItSubscribeServerResource.showMessage();
        assertThat(message.contains("HTTP"), is(true));
    }
}
