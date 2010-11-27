package com.thoughtworks.blipit.persistance;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class Persist {
    private static final PersistenceManagerFactory instance =
            JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private Persist() {
    }

    public static PersistenceManager getPersistenceManager() {
        return instance.getPersistenceManager();
    }
}