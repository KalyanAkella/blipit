package com.thoughtworks.blipit.utils;

import com.thoughtworks.contract.BlipItResource;
import org.restlet.resource.ClientResource;

public class BlipItServiceHelper {

    private static final String BLIPIT_SERVICE_URI = "http://10.0.2.2:8080/blipit";
    private static BlipItResource blipItResource;

    public BlipItServiceHelper() {
        ClientResource clientResource = new ClientResource(BLIPIT_SERVICE_URI);
        blipItResource = clientResource.wrap(BlipItResource.class);
    }

    public BlipItResource getBlipItResource() {
        return blipItResource;
    }
}
