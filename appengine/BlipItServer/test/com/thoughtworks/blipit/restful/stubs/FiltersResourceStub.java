package com.thoughtworks.blipit.restful.stubs;

import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.domain.Filter;
import com.thoughtworks.blipit.restful.FiltersResource;
import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FiltersResourceStub extends FiltersResource {
    private String category;

    public FiltersResourceStub(String category) {
        this.category = category;
    }

    @Override
    public Map<String, Object> getRequestAttributes() {
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("category", category);
        return attributes;
    }

    public Filter performPost(Filter filter) throws JSONException, IOException {
        doInit();
        String filterJson = gson.toJson(filter);
        Representation representation = super.post(new JsonRepresentation(filterJson), new Variant(MediaType.APPLICATION_JSON));
        Filter result = null;
        if (representation instanceof JsonRepresentation) {
            JsonRepresentation jsonRepresentation = (JsonRepresentation) representation;
            String jsonStr = jsonRepresentation.getText();
            System.out.println(jsonStr);
            result = gson.fromJson(jsonStr, new TypeToken<Filter>(){}.getType());
        }
        return result;
    }
}
