package com.thoughtworks.blipit.domain;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable
public class Alert {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;

    @Persistent
    private String source;

    @Persistent
    private String description;

    @Persistent
    private GeoPt geoPoint;

    public Alert(String source, String description, GeoPt geoPoint) {
        this.source = source;
        this.description = description;
        this.geoPoint = geoPoint;
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

    public GeoPt getGeoPoint() {
        return geoPoint;
    }

    public boolean isSameAs(Alert alert) {
        return this.getSource().equals(alert.getSource()) &&
                this.getDescription().equalsIgnoreCase(alert.getDescription()) &&
                this.getGeoPoint().compareTo(alert.getGeoPoint()) == 0;
    }
}
