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
import java.util.List;
import java.util.Set;

import static com.thoughtworks.blipit.Utils.constructCategorySet;

public class FiltersResource extends ServerResource {

    private Set<Category> categorySet;
    private BlipItRepository blipItRepository;
    protected Gson gson;

    @Override
    protected void doInit() throws ResourceException {
        this.getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        this.categorySet = constructCategorySet((String) getRequestAttributes().get("category"));
        this.blipItRepository = new BlipItRepository();
        this.gson = new Gson();
    }

    // TODO: Send error representation on errors
    @Override
    protected Representation get(Variant variant) throws ResourceException {
        List<Filter> filters = blipItRepository.retrieveFiltersByCategories(categorySet);
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
                Filter filter = gson.fromJson(entity.getText(), Filter.class);
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
