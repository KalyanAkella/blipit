package com.thoughtworks.blipit.persistance;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class DataStoreHelper {
    private static final String PERSISTENCE_PROP_NAME = "transactions-optional";
    private static PersistenceManagerFactory factory = JDOHelper.getPersistenceManagerFactory(PERSISTENCE_PROP_NAME);

    private DataStoreHelper() {
    }

    public static PersistenceManager getPersistenceManager() {
        return factory.getPersistenceManager();
    }

    public static <T> T save(T instance) {
        PersistenceManager persistenceManager = null;
        try {
            persistenceManager = DataStoreHelper.getPersistenceManager();
            return persistenceManager.makePersistent(instance);
        } finally {
            if (persistenceManager != null) persistenceManager.close();
        }
    }
}