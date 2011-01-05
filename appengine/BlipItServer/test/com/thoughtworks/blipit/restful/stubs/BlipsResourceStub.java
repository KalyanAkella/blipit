package com.thoughtworks.blipit.restful.stubs;

import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.restful.BlipsResource;
import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlipsResourceStub extends BlipsResource {
    private String category;

    public BlipsResourceStub(String category) {
        this.category = category;
    }

    @Override
    public Map<String, Object> getRequestAttributes() {
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("category", category);
        return attributes;
    }

    public List<Blip> performGet() throws JSONException, IOException {
        doInit();
        Representation representation = super.get(new Variant(MediaType.APPLICATION_JSON));
        List<Blip> result = null;
        if (representation instanceof JsonRepresentation) {
            JsonRepresentation jsonRepresentation = (JsonRepresentation) representation;
            String jsonStr = jsonRepresentation.getText();
            System.out.println(jsonStr);
            result = gson.fromJson(jsonStr, new TypeToken<List<Blip>>(){}.getType());
        }
        return result;
    }
}
