package com.thoughtworks.blipit;

import org.junit.Assert;
import org.junit.Test;

public class TransportLayerImplTest {
    
    @org.junit.Test
    public void testGetBlipTitle() throws Exception {
        TransportLayerImpl transportLayer = new TransportLayerImpl();
        Assert.assertEquals(transportLayer.getBlipTitle(), "Blip It");  
    }
    
    @Test
    public void testGetBlipSnippet() throws Exception {
        TransportLayerImpl transportLayer = new TransportLayerImpl();
        Assert.assertEquals(transportLayer.getBlipSnippet(), "Snippet");
    }
}
