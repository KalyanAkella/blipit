package com.thoughtworks.blipit.utils;

import com.thoughtworks.contract.BlipItSubscribeResource;
import org.restlet.resource.ClientResource;

public class BlipItServiceHelper {

    private static final String BLIPIT_SERVICE_URI = "http://10.0.2.2:8080/blipit/subscribe";
    private static BlipItSubscribeResource blipItResource;

    public BlipItServiceHelper() {
        ClientResource clientResource = new ClientResource(BLIPIT_SERVICE_URI);
        blipItResource = clientResource.wrap(BlipItSubscribeResource.class);
    }

    public BlipItSubscribeResource getBlipItResource() {
        return blipItResource;
    }
}
