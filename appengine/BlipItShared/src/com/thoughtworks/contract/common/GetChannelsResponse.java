package com.thoughtworks.contract.common;

import com.thoughtworks.contract.BlipItResponse;

import java.util.ArrayList;
import java.util.List;

public class GetChannelsResponse extends BlipItResponse {
    private List<Channel> channels;

    public GetChannelsResponse() {
        channels = new ArrayList<Channel>();
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void addChannel(String id, String name, String desc) {
        Channel newChannel = new Channel(id, name, desc);
        if (channels.contains(newChannel)) return;
        channels.add(newChannel);
    }
}
