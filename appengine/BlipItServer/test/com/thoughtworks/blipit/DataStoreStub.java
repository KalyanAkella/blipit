package com.thoughtworks.blipit;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

import javax.jdo.annotations.Persistent;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.fail;

public class DataStoreStub {

    private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    public Key setupEntityAsPersisted(Object entity) {
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
                if(!isAnnotationPresentOnField(field, Persistent.class)) continue;
                entityToPersist.setProperty(field.getName(), field.get(entity));
            } catch (IllegalAccessException e) {
                System.out.println("Exception while setting up test data:" + e);
                fail();
            }
        }
    }

    private boolean isAnnotationPresentOnField(Field field, Class clazz){
        final Annotation annotation = field.getAnnotation(clazz);
        return annotation != null;
    }
}
