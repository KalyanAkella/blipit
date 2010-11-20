package com.thoughtworks.contract;

import java.io.Serializable;

public class BlipItResponse implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
