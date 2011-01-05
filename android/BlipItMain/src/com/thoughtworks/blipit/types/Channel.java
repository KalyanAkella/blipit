package com.thoughtworks.blipit.types;

import java.io.Serializable;

public class Channel implements Serializable {
    private Key key;
    private String name;
    private ChannelCategory category;

    public Channel() {
        this(null, null, null);
    }

    public Channel(Key key, String name, ChannelCategory category) {
        this.key = key;
        this.name = name;
        this.category = category;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChannelCategory getCategory() {
        return category;
    }

    public void setCategory(ChannelCategory category) {
        this.category = category;
    }
}
