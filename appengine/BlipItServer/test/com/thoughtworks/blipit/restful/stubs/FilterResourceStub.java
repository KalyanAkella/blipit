package com.thoughtworks.blipit.restful.stubs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.restful.FilterResource;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterResourceStub extends FilterResource {
    private String filterId;

    public FilterResourceStub(String filterId) {
        this.filterId = filterId;
    }

    @Override
    public Map<String, Object> getRequestAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("filter_id", filterId);
        return attributes;
    }

    public List<Blip> performGet() throws IOException {
        doInit();
        Representation representation = super.get(new Variant(MediaType.APPLICATION_JSON));
        List<Blip> blips = null;
        if (representation instanceof JsonRepresentation) {
            JsonRepresentation jsonRepresentation = (JsonRepresentation) representation;
            String jsonStr = jsonRepresentation.getText();
            System.out.println(jsonStr);
            blips = new Gson().fromJson(jsonStr, new TypeToken<List<Blip>>(){}.getType());
        }
        return blips;
    }

    public boolean performDelete() throws IOException {
        doInit();
        Representation representation = super.delete(new Variant(MediaType.APPLICATION_JSON));
        String resultStr = representation.getText();
        System.out.println(resultStr);
        return resultStr != null && resultStr.toLowerCase().contains("success");
    }
}
