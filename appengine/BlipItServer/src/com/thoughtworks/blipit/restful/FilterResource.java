package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.blipit.domain.Blip;
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
import java.lang.reflect.Type;
import java.util.List;

public class FilterResource extends ServerResource {

    private String filterId;
    private BlipItRepository blipItRepository;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.filterId = (String) getRequestAttributes().get("filter_id");
        blipItRepository = new BlipItRepository();
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        Filter filter = loadFilter();
        if (filter == null) return new StringRepresentation("No filter found with the given ID: " + filterId);
        List<Blip> filteredBlips = filter.filterBlips(blipItRepository);
        if (filteredBlips == null) return new StringRepresentation("No blips found for the given filter ID: " + filterId);
        Type listOfBlipsType = new TypeToken<List<Blip>>() {}.getType();
        return new JsonRepresentation(new Gson().toJson(filteredBlips, listOfBlipsType));
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation delete(Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        try {
            Key key = KeyFactory.createKey(Filter.class.getSimpleName(), Long.valueOf(filterId));
            persistenceManager = DataStoreHelper.getPersistenceManager();
            Filter filter = persistenceManager.getObjectById(Filter.class, key);
            if (filter == null) return new StringRepresentation("No blip found with ID: " + filterId);
            persistenceManager.deletePersistent(filter);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
        return new JsonRepresentation(String.format("Success"));
    }

    private Filter loadFilter() {
        PersistenceManager persistenceManager = null;
        Filter filter = null;
        try {
            Key key = KeyFactory.createKey(Filter.class.getSimpleName(), Long.valueOf(filterId));
            persistenceManager = DataStoreHelper.getPersistenceManager();
            filter = persistenceManager.getObjectById(Filter.class, key);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
        return filter;
    }

}
