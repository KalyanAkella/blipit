package com.thoughtworks.blipit;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataStoreStubTest {

    protected LocalServiceTestHelper helper;

    @Before
    public void setup() {
        helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
        helper.setUp();
        doSetup();
    }

    @After
    public void tearDown() {
        doTearDown();
        helper.tearDown();
    }

    //Override this in derived class to add any test fixture specific setup
    protected void doSetup() {
    }

    //Override this in derived class to add any test fixture specific teardown
    protected void doTearDown() {
    }

    //TODO: Find a way of making this class, doSetup and doTearDown abstract and remove this method.
    @Test
    public void dummy() {
        //Do not delete till the TODO is complete.
        //Its a hack to work around jUnit failing when there are no runnable tests in
        //a class which has @Before and @After methods.
    }
}
