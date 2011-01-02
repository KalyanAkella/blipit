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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.thoughtworks.blipit.domain.Blip;
import com.thoughtworks.blipit.domain.Channel;

import javax.jdo.annotations.Persistent;
import java.lang.reflect.Field;

import static org.junit.Assert.fail;

public class DataStoreStub {

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    private Key setupEntityAsPersisted(Object entity) {
        Entity entityToPersist = new Entity(entity.getClass().getSimpleName());
        setPersistentFieldValuesFromEntity(entity, entityToPersist);
        return ds.put(entityToPersist);
    }

    private void setPersistentFieldValuesFromEntity(Object entity, Entity entityToPersist) {
        final Field[] fields = entity.getClass().getDeclaredFields();
        for(Field field : fields){
            try {
                field.setAccessible(true);
                //TODO : Change this to use field.IsAnnotationPresent method <K3>
                if(field.getAnnotation(Persistent.class) == null) continue;
                entityToPersist.setProperty(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                System.out.println("Exception while setting up test data:" + e);
                fail();
            }
        }
    }

    public void makePersistent(Blip blip) {
        blip.setKey(setupEntityAsPersisted(blip));
    }

    public void makePersistent(Channel channel) {
        channel.setKey(setupEntityAsPersisted(channel));
    }
}
