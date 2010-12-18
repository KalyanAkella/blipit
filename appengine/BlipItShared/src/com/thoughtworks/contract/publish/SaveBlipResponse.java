package com.thoughtworks.contract.publish;

import com.thoughtworks.contract.BlipItError;

import java.io.Serializable;

public class SaveBlipResponse implements Serializable {
    private BlipItError blipItError;
    private boolean status;

    public BlipItError getBlipItError() {
        return blipItError;
    }

    public boolean isFailure() {
        return !status;
    }

    public void setSuccess() {
        this.status = true;
    }

    public void setFailure(BlipItError blipItError) {
        this.status = false;
        this.blipItError = blipItError;
    }
}
