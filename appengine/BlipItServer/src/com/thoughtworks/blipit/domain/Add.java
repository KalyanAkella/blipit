package com.thoughtworks.blipit.domain;

import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class Add {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String source;

    @Persistent
    private String description;

    public Add(String source, String description) {
        this.source = source;
        this.description = description;
    }

    public Key getKey() {
        return key;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
    }
}
