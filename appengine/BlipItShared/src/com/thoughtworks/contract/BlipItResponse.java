package com.thoughtworks.contract;

import java.io.Serializable;
import java.util.List;

public class BlipItResponse implements Serializable {
    private List<Blip> blips;
    private BlipItError blipItError;

    public List<Blip> getBlips() {
        return blips;
    }

    public void setBlips(List<Blip> blips) {
        this.blips = blips;
    }

    public BlipItError getBlipItError() {
        return blipItError;
    }

    public void setBlipItError(BlipItError blipItError) {
        this.blipItError = blipItError;
    }
}
