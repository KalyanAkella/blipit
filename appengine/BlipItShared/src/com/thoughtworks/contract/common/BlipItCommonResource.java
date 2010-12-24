package com.thoughtworks.contract.common;

import org.restlet.resource.Post;

public interface BlipItCommonResource {
    @Post
    public GetChannelsResponse getPanicChannels();

    @Post
    public GetChannelsResponse getFreeChannels();
}
