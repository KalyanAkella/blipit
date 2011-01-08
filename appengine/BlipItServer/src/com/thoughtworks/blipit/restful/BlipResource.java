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
        this.blipId = (String) getRequestAttributes().get("blip_id");
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
