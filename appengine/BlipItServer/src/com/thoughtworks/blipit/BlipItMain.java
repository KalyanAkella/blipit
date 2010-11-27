package com.thoughtworks.blipit;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BlipItMain extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/blipit", BlipItServerResource.class);
        return router;
    }
}
