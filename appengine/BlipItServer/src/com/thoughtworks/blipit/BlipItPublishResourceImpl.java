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

package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.persistence.DataStoreHelper;
import com.thoughtworks.contract.BlipItResponse;
import com.thoughtworks.contract.Constants;
import com.thoughtworks.contract.common.Category;
import com.thoughtworks.contract.common.Channel;
import com.thoughtworks.contract.common.GetChannelsResponse;
import com.thoughtworks.contract.publish.BlipItPublishResource;
import com.thoughtworks.contract.publish.DeleteBlipRequest;
import com.thoughtworks.contract.publish.SaveBlipRequest;
import com.thoughtworks.contract.publish.SaveBlipResponse;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlipItPublishResourceImpl extends BlipItCommonServerResource implements BlipItPublishResource {
    private static final Logger log = Logger.getLogger(BlipItPublishResourceImpl.class.getName());

    public BlipItPublishResourceImpl() {
        super();
    }

    @Post
    public SaveBlipResponse saveBlip(SaveBlipRequest saveBlipRequest) {
        final SaveBlipResponse saveBlipResponse = new SaveBlipResponse();
        Blip blip = constructBlip(saveBlipRequest, saveBlipResponse);
        if (saveBlipResponse.isSuccess()) {
            DataStoreHelper.save(blip, new Utils.ResultHandler<Blip>() {
                public void onSuccess(Blip savedBlip) {
                    saveBlipResponse.setSuccess();
                    String keyAsString = savedBlip.getKeyAsString();
                    saveBlipResponse.setBlipId(keyAsString);
                    log.info("Blip with title, " + savedBlip.getTitle() + " saved successfully with ID: " + keyAsString);
                }

                public void onError(Throwable throwable) {
                    saveBlipResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
                    log.log(Level.SEVERE, "Blip creation failed with error", throwable);
                }
            });
        }
        return saveBlipResponse;
    }

    // TODO: This method needs some refactoring !!!
    // TODO: This method needs an integration test
    private Blip constructBlip(SaveBlipRequest saveBlipRequest, SaveBlipResponse saveBlipResponse) {
        Blip blip = null;
        PersistenceManager persistenceManager = null;

        try {
            persistenceManager = DataStoreHelper.getPersistenceManager();
            Set<Key> channelKeys = loadChannelKeys(saveBlipRequest);

            String title = saveBlipRequest.getMetaDataValue(Constants.TITLE);
            String description = saveBlipRequest.getMetaDataValue(Constants.DESC);
            String creatorId = getCreatorId(saveBlipRequest);
            GeoPt blipLoc = Utils.asGeoPoint(saveBlipRequest.getBlipLocation());

            if (saveBlipRequest.isPanicRequest()) {
                blip = loadBlipByCreatorId(creatorId, persistenceManager);
            }

            if (blip != null) {
                blip.setTitle(title);
                blip.setDescription(description);
                blip.setGeoPoint(blipLoc);
                blip.setChannelKeys(channelKeys);
                blip.setCreatorId(creatorId);
            }
            else {
                blip = new Blip(title, description, blipLoc, channelKeys, creatorId);
            }
            saveBlipResponse.setSuccess();
        } catch (Exception e) {
            saveBlipResponse.setFailure(Utils.getBlipItError(e.getMessage()));
            log.log(Level.SEVERE, "Blip creation failed with error", e);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }

        return blip;
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

    private String getCreatorId(SaveBlipRequest saveBlipRequest) {
        return String.format("%s:%s", saveBlipRequest.getMetaDataValue(Constants.DEVICE_ID), saveBlipRequest.getMetaDataValue(Constants.PHONE_NUMBER));
    }

    private Set<Key> loadChannelKeys(SaveBlipRequest saveBlipRequest) {
        List<Channel> channels = saveBlipRequest.getChannels();
        Set<Key> channelKeys = new HashSet<Key>();
        for (Channel channel : channels) {
            channelKeys.add(mapToChannelKey(channel));
        }
        return channelKeys;
    }

    @Delete
    public BlipItResponse deleteBlip(DeleteBlipRequest deleteBlipRequest) {
        final String blipId = deleteBlipRequest.getBlipId();
        final BlipItResponse blipItResponse = new BlipItResponse();
        DataStoreHelper.delete(Blip.class, blipId, new Utils.ResultHandler<Blip>() {
            public void onSuccess(Blip arg) {
                blipItResponse.setSuccess();
                log.info("Blip with ID, " + blipId + " deleted successfully");
            }

            public void onError(Throwable throwable) {
                blipItResponse.setFailure(Utils.getBlipItError(throwable.getMessage()));
                log.log(Level.SEVERE, "Blip deletion failed with error", throwable);
            }
        });
        return blipItResponse;
    }

    private Key mapToChannelKey(Channel channel) {
        return KeyFactory.stringToKey(channel.getId());
    }

    @Get
    public GetChannelsResponse getAvailableChannels(Category channelCategory) {
        return getChannels(channelCategory);
    }
}
