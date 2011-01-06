package com.thoughtworks.blipit.restful.stubs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.restful.BlipResource;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BlipResourceStub extends BlipResource {
    private String blipId;

    public BlipResourceStub(String blipId) {
        this.blipId = blipId;
    }

    @Override
    public Map<String, Object> getRequestAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("blip_id", blipId);
        return attributes;
    }

    public Blip performGet() throws IOException {
        doInit();
        Representation representation = super.get(new Variant(MediaType.APPLICATION_JSON));
        Blip result = null;
        if (representation instanceof JsonRepresentation) {
            JsonRepresentation jsonRepresentation = (JsonRepresentation) representation;
            String jsonStr = jsonRepresentation.getText();
            System.out.println(jsonStr);
            result = new Gson().fromJson(jsonStr, new TypeToken<Blip>(){}.getType());
        }
        return result;
    }

    public boolean performDelete() throws IOException {
        doInit();
        Representation representation = super.delete(new Variant(MediaType.APPLICATION_JSON));
        String resultStr = representation.getText();
        System.out.println(resultStr);
        return resultStr != null && resultStr.toLowerCase().contains("success");
    }
}
