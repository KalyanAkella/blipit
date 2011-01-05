package com.thoughtworks.blipit.panicblip.types;

import java.util.HashSet;
import java.util.Set;

public class Panic {
    private Key key;
    private String title;
    private String description;
    private Location geoPoint;
    private String creatorId;
    private Set<Key> channelKeys;

    public Panic() {
        this(null, null, null, null, null, new HashSet<Key>());
    }

    public Panic(Key key, String title, String description, Location geoPoint, String creatorId, Set<Key> channelKeys) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.geoPoint = geoPoint;
        this.channelKeys = channelKeys;
        this.creatorId = creatorId;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(Location geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Set<Key> getChannelKeys() {
        return channelKeys;
    }

    public void setChannelKeys(Set<Key> channelKeys) {
        this.channelKeys = channelKeys;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
