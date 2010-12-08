package com.thoughtworks.contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlipItResponse implements Serializable {
    private List<Blip> blips;
    private BlipItError blipItError;

    public BlipItResponse() {
        blips = new ArrayList<Blip>();
    }

    public List<Blip> getBlips() {
        return blips;
    }

    public void addBlips(Blip... blips) {
        this.blips.addAll(Arrays.asList(blips));
    }

    public BlipItError getBlipItError() {
        return blipItError;
    }

    public void setBlipItError(BlipItError blipItError) {
        this.blipItError = blipItError;
    }

    public boolean hasError() {
        return blipItError != null;
    }

    public boolean hasNoError() {
        return !hasError();
    }
}
