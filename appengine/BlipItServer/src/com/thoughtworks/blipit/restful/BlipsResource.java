package com.thoughtworks.blipit.restful;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.Gson;
import com.thoughtworks.blipit.Utils;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.domain.Channel;
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
import javax.jdo.Query;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlipsResource extends ServerResource {

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

    @Override
    protected Representation get(Variant variant) throws ResourceException {
        Category category = Utils.convert(com.thoughtworks.contract.common.Category.valueOf(categoryStr.toUpperCase()));
        List<Blip> blips = blipItRepository.retrieveBlipsByCategory(category);
        String json = gson.toJson(blips);
        return Utils.isJSONMediaType(variant) ? new JsonRepresentation(json) : new StringRepresentation(json);
    }

    // TODO: Send error representation on errors
    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        if (Utils.isJSONMediaType(variant)) {
            try {
                persistenceManager = DataStoreHelper.getPersistenceManager();
                Blip blip = gson.fromJson(new InputStreamReader(entity.getStream()), Blip.class);
                prepareKeys(blip);
                if (isPanicFlow()) blip = loadBlipIfRequired(blip, persistenceManager);
                String json = gson.toJson(persistenceManager.makePersistent(blip));
                return new JsonRepresentation(json);
            } catch (IOException e) {
                return new StringRepresentation(e.getMessage());
            } finally {
                if (persistenceManager != null) persistenceManager.close();
            }
        }
        return new StringRepresentation("Unsupported media type: " + variant.getMediaType());
    }

    private Blip loadBlipIfRequired(Blip givenBlip, PersistenceManager persistenceManager) {
        Blip loadedBlip = loadBlipByCreatorId(givenBlip.getCreatorId(), persistenceManager);
        if (loadedBlip == null) loadedBlip = givenBlip;
        else {
            loadedBlip.copy(givenBlip);
        }
        return loadedBlip;
    }

    private boolean isPanicFlow() {
        return "panic".equals(categoryStr);
    }

    private Blip loadBlipByCreatorId(String creatorId, PersistenceManager persistenceManager) {
        Query query = null;
        Blip result = null;
        try {
            query = persistenceManager.newQuery(Blip.class);
            query.declareParameters("String creatorId");
            query.setFilter("this.creatorId == creatorId");
            List<Blip> blipsFromCreator = (List<Blip>) query.execute(creatorId);
            if (Utils.isNotEmpty(blipsFromCreator)) {
                result = blipsFromCreator.get(0);
            }
        } finally {
            if (query != null) query.closeAll();
        }
        return result;
    }

    private void prepareKeys(Blip blip) {
        Key blipKey = blip.getKey();
        if (blipKey != null) {
            blip.setKey(KeyFactory.createKey(Blip.class.getSimpleName(), blipKey.getId()));
        }
        prepareChannelKeys(blip);
    }

    private void prepareChannelKeys(Blip blip) {
        Set<Key> channelKeys = new HashSet<Key>();
        if (Utils.isNotEmpty(blip.getChannelKeys())) {
            for (Key channelKey : blip.getChannelKeys()) {
                channelKeys.add(KeyFactory.createKey(Channel.class.getSimpleName(), channelKey.getId()));
            }
        }
        blip.setChannelKeys(channelKeys);
    }
}
