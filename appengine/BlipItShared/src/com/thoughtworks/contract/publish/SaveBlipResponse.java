package com.thoughtworks.contract.publish;

import com.thoughtworks.contract.BlipItResponse;

public class SaveBlipResponse extends BlipItResponse {
    private String blipId;

    public String getBlipId() {
        return blipId;
    }

    public void setBlipId(String blipId) {
        this.blipId = blipId;
    }
}
