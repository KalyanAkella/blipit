package com.thoughtworks.blipit.panicblip.types;

import java.io.Serializable;

public class Key implements Serializable {
    private String kind;
    private String id;

    public Key() {
        this(null, null);
    }

    public Key(String kind, String id) {
        this.kind = kind;
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
