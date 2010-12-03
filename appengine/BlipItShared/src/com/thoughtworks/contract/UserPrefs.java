package com.thoughtworks.contract;

import java.util.List;

public class UserPrefs {
    private double radius;
    private List<String> channels;

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
