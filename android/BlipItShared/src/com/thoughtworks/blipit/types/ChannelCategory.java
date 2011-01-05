package com.thoughtworks.blipit.types;

import java.io.Serializable;

public class ChannelCategory implements Serializable {
    private String category;

    public ChannelCategory() {
        this(null);
    }

    public ChannelCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
