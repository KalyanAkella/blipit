package com.thoughtworks.blipit;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractDataStoreStubTest {

    protected LocalServiceTestHelper helper;
    protected DataStoreStub dataStoreStub;

    @Before
    public void before() {
        helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
        helper.setUp();
        dataStoreStub = new DataStoreStub();
    }

    @After
    public void after() {
        helper.tearDown();
    }
}
