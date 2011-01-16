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

package com.thoughtworks.blipit.restful.stubs;

import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.domain.Channel;
import com.thoughtworks.blipit.restful.ChannelsResource;
import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChannelsResourceStub extends ChannelsResource {
    private String category;

    public ChannelsResourceStub(String category) {
        this.category = category;
    }

    @Override
    public Map<String, Object> getRequestAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("category", category);
        return attributes;
    }

    public List<Channel> performGet() throws JSONException, IOException {
        doInit();
        Representation representation = super.get(new Variant(MediaType.APPLICATION_JSON));
        List<Channel> result = null;
        if (representation instanceof JsonRepresentation) {
            JsonRepresentation jsonRepresentation = (JsonRepresentation) representation;
            String jsonStr = jsonRepresentation.getText();
            System.out.println(jsonStr);
            result = gson.fromJson(jsonStr, new TypeToken<List<Channel>>() {}.getType());
        }
        return result;
    }

    public Channel performPost(Channel channel) throws JSONException, IOException {
        doInit();
        String channelJson = gson.toJson(channel);
        Representation representation = super.post(new JsonRepresentation(channelJson), new Variant(MediaType.APPLICATION_JSON));
        Channel result = null;
        if (representation instanceof JsonRepresentation) {
            JsonRepresentation jsonRepresentation = (JsonRepresentation) representation;
            String jsonStr = jsonRepresentation.getText();
            System.out.println(jsonStr);
            result = gson.fromJson(jsonStr, new TypeToken<Channel>(){}.getType());
        }
        return result;
    }
}
