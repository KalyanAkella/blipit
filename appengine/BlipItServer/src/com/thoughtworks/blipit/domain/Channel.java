package com.thoughtworks.blipit.domain;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Channel {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String name;

    @Persistent
    private String description;

    @Persistent
    private ChannelCategory category;

    public Channel() {
        this(null, null, null, null);
    }

    public Channel(Key key, String name, String description, ChannelCategory category) {
        this.key = key;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ChannelCategory getCategory() {
        return category;
    }

    public void setCategory(ChannelCategory category) {
        this.category = category;
    }

    public String getKeyAsString() {
        return KeyFactory.keyToString(key);
    }
}
