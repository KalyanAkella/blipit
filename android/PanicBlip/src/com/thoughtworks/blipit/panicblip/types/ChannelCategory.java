package com.thoughtworks.blipit.panicblip.types;

public class ChannelCategory {
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
