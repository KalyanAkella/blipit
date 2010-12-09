/*
 * Copyright (c) 2010 BlipIt Committers
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

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
