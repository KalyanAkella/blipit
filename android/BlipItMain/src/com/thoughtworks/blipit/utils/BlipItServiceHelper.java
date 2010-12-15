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

package com.thoughtworks.blipit.utils;

import com.thoughtworks.contract.BlipItSubscribeResource;
import org.restlet.resource.ClientResource;

public class BlipItServiceHelper {

    public static BlipItSubscribeResource getSubscribeResource(String blipitServiceUri) {
        ClientResource clientResource = new ClientResource(blipitServiceUri);
        return clientResource.wrap(BlipItSubscribeResource.class);
    }
}
