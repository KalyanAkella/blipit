package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Category;
import com.google.gson.Gson;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Filter;
import com.thoughtworks.blipit.persistence.BlipItRepository;
import com.thoughtworks.blipit.persistence.DataStoreHelper;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.jdo.PersistenceManager;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class FiltersResource extends ServerResource {

    private String categoryStr;
    private BlipItRepository blipItRepository;
    private Gson gson;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.categoryStr = ((String) getRequest().getAttributes().get("category")).toLowerCase();
        blipItRepository = new BlipItRepository();
        gson = new Gson();
    }

    // TODO: Send error representation on errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        Category category = Utils.convert(com.thoughtworks.contract.common.Category.valueOf(categoryStr.toUpperCase()));
        List<Filter> filters = blipItRepository.retrieveFiltersByCategory(category);
        String json = gson.toJson(filters);
        return Utils.isJSONMediaType(variant) ? new JsonRepresentation(json) : new StringRepresentation(json);
    }

    // TODO: Send error representation on errors
    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        if (Utils.isJSONMediaType(variant)) {
            try {
                persistenceManager = DataStoreHelper.getPersistenceManager();
                Filter filter = gson.fromJson(new InputStreamReader(entity.getStream()), Filter.class);
                filter.prepareKeys();
                String json = gson.toJson(persistenceManager.makePersistent(filter));
                return new JsonRepresentation(json);
            } catch (IOException e) {
                return new StringRepresentation(e.getMessage());
            } finally {
                if (persistenceManager != null) persistenceManager.close();
            }
        }
        return new StringRepresentation("Unsupported media type: " + variant.getMediaType());
    }

}
