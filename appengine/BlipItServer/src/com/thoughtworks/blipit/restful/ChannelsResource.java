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

package com.thoughtworks.blipit.restful;

import com.google.gson.Gson;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.persistence.BlipItRepository;
import com.thoughtworks.blipit.domain.CategoryEnum;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.List;

public class ChannelsResource extends ServerResource {
    private String category;
    private BlipItRepository blipItRepository;
    protected Gson gson;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.category = (String) getRequestAttributes().get("category");
        this.blipItRepository = new BlipItRepository();
        this.gson = new Gson();
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        CategoryEnum channelCategory = CategoryEnum.valueOf(category.toUpperCase());
        List<Channel> channels = blipItRepository.retrieveChannelsByCategory(Utils.convert(channelCategory));
        String json = gson.toJson(channels);
        if (variant.getMediaType().equals(MediaType.APPLICATION_JSON))
            return new JsonRepresentation(json);
        return new StringRepresentation(json);
    }
}
