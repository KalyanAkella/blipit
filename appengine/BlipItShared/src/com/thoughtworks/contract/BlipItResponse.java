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

public class BlipItResponse implements Serializable {
    private BlipItError blipItError;
    private boolean status;

    public BlipItError getBlipItError() {
        return blipItError;
    }

    public boolean isFailure() {
        return !status;
    }

    public boolean isSuccess() {
        return status;
    }

    public void setSuccess() {
        this.status = true;
    }

    public void setFailure(BlipItError blipItError) {
        this.status = false;
        this.blipItError = blipItError;
    }
}
