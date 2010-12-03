package com.thoughtworks.contract;

import java.io.Serializable;
import java.util.Date;

public class Blip implements Serializable {
    private String title;
    private String message;
    private GeoLocation blipLocation;
    private Date expiryDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public GeoLocation getBlipLocation() {
        return blipLocation;
    }

    public void setBlipLocation(GeoLocation blipLocation) {
        this.blipLocation = blipLocation;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
