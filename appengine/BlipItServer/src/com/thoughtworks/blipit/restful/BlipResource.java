package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.persistence.DataStoreHelper;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.representation.Variant;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.jdo.PersistenceManager;

public class BlipResource extends ServerResource {

    private String blipId;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.blipId = (String) getRequest().getAttributes().get("blip_id");
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        Blip blip = null;
        try {
            Key key = KeyFactory.createKey(Blip.class.getSimpleName(), Long.valueOf(blipId));
            persistenceManager = DataStoreHelper.getPersistenceManager();
            blip = persistenceManager.getObjectById(Blip.class, key);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
        if (blip == null) return new StringRepresentation("No blip found with ID: " + blipId);
        return new JsonRepresentation(new Gson().toJson(blip));
    }

    // TODO: Send ErrorRepresentation in case of errors
    @Override
    protected Representation delete(Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        try {
            Key key = KeyFactory.createKey(Blip.class.getSimpleName(), Long.valueOf(blipId));
            persistenceManager = DataStoreHelper.getPersistenceManager();
            Blip blip = persistenceManager.getObjectById(Blip.class, key);
            if (blip == null) return new StringRepresentation("No blip found with ID: " + blipId);
            persistenceManager.deletePersistent(blip);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
        return new JsonRepresentation(String.format("Success"));
    }
}
