package com.thoughtworks.contract.publish;

import java.io.Serializable;

public class DeleteBlipRequest implements Serializable {
    private String blipId;

    public String getBlipId() {
        return blipId;
    }

    public void setBlipId(String blipId) {
        this.blipId = blipId;
    }
}
