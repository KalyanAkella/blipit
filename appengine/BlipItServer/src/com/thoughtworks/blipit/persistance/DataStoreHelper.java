package com.thoughtworks.blipit.persistance;

import com.thoughtworks.blipit.Utils;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataStoreHelper {
    private static final String PERSISTENCE_PROP_NAME = "transactions-optional";
    private static PersistenceManagerFactory factory = JDOHelper.getPersistenceManagerFactory(PERSISTENCE_PROP_NAME);
    private static final Logger log = Logger.getLogger(DataStoreHelper.class.getName());

    private DataStoreHelper() {
    }

    public static PersistenceManager getPersistenceManager() {
        return factory.getPersistenceManager();
    }

    public static <T> T save(T instance) {
        PersistenceManager persistenceManager = null;
        try {
            persistenceManager = getPersistenceManager();
            return persistenceManager.makePersistent(instance);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
    }

    public static <T> void retrieveAllAndProcess(Class<T> clazz, Utils.Task<T> forEachElement, Utils.Task<Throwable> onError) {
        PersistenceManager persistenceManager = null;
        Query query = null;
        try {
            persistenceManager = getPersistenceManager();
            query = persistenceManager.newQuery(clazz);
            List<T> elements = (List<T>) query.execute();
            if (Utils.isNotEmpty(elements)) {
                for (T element : elements) {
                    forEachElement.perform(element);
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error occured while fetching elements of type " + clazz.getSimpleName() + " from data store", e);
            onError.perform(e);
        } finally {
            if (query != null) query.closeAll();
            if (persistenceManager != null) persistenceManager.close();
        }
    }
}