package com.thoughtworks.contract.utils;

import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.common.Channel;

import java.util.ArrayList;
import java.util.List;

public class ChannelUtils {

    public static final String CHANNEL_SPLITTER = "\\|";
    public static final String CHANNEL_SEPARATOR = "|";
    public static final String ID_NAME_SEPARATOR = ":";

    private ChannelUtils() {
    }

    public static String getChannelsAsString(List<Channel> channelList) {
        StringBuilder buffer = new StringBuilder();
        for (Channel channel : channelList) {
            buffer.append(channel.getId());
            buffer.append(ID_NAME_SEPARATOR);
            buffer.append(channel.getName());
            buffer.append(CHANNEL_SEPARATOR);
        }
        if (buffer.length() > 0) buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

    public static List<Channel> toChannelList(String channelsStr, Category category) {
        List<Channel> channelList = new ArrayList<Channel>();
        if (channelsStr != null) {
            String[] channels = channelsStr.split(CHANNEL_SPLITTER);
            if (channels != null && channels.length > 0) {
                for (String channel : channels) {
                    String[] idName = channel.split(ID_NAME_SEPARATOR);
                    if (idName.length == 2) channelList.add(new Channel(idName[0], idName[1], idName[1], category));
                }
            }
        }
        return channelList;
    }

    public static List<String> toChannelNames(List<Channel> channels) {
        List<String> channelNames = new ArrayList<String>();
        if (channels != null) {
            for (Channel availableChannel : channels) {
                channelNames.add(availableChannel.getName());
            }
        }
        return channelNames;
    }
}
