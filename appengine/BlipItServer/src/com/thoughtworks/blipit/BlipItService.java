package com.thoughtworks.blipit;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class BlipItService extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/subscribe", BlipItSubscribeServerResource.class);
        router.attach("/publish", BlipItPublishServerResource.class);
        return router;
    }
}