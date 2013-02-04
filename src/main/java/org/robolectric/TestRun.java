package org.robolectric;

import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: matiz
 * Date: 2/3/13
 * Time: 10:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TestRun {
    /*
         * Called before each test method is run. Sets up the simulation of the Android runtime environment.
         */
    void internalBeforeTest();

    void internalAfterTest();

    void beforeTest(Method method);

    void afterTest(Method method);

    void prepareTest(Object test);

    void setupApplicationState(Method testMethod);
}
