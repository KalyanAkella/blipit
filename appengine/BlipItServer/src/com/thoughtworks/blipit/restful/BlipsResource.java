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
        this.categoryStr = (String) getRequest().getAttributes().get("category");
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

    // TODO: Imp !!! Check the incoming blip for creatorID and update the existing blip for category PANIC
    @Override
    protected Representation post(Representation entity, Variant variant) throws ResourceException {
        PersistenceManager persistenceManager = null;
        if (Utils.isJSONMediaType(variant)) {
            try {
                persistenceManager = DataStoreHelper.getPersistenceManager();
                Blip blip = gson.fromJson(new InputStreamReader(entity.getStream()), Blip.class);
                prepareKeys(blip);
                Blip savedBlip = persistenceManager.makePersistent(blip);
                String json = gson.toJson(savedBlip);
                return new JsonRepresentation(json);
            } catch (IOException e) {
                return new StringRepresentation(e.getMessage());
            } finally {
                if (persistenceManager != null) persistenceManager.close();
            }
        }
        return new StringRepresentation("Unsupported media type: " + variant.getMediaType());
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
