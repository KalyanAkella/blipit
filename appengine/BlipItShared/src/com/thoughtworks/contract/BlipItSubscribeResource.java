package com.thoughtworks.contract;

import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface BlipItSubscribeResource {
    @Get
    public String showMessage();

    @Post
    public BlipItResponse echo(BlipItRequest blipItRequest);
}
