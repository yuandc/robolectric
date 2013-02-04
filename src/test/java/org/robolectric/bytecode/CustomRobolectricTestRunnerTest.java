package org.robolectric.bytecode;

import android.app.Application;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.robolectric.DefaultTestRun;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricContext;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(CustomRobolectricTestRunnerTest.CustomConfig.class)
public class CustomRobolectricTestRunnerTest {
    Object preparedTest;
    static Method testMethod;
    static int beforeCallCount = 0;
    static int afterTestCallCount = 0;

    @Before
    public void setUp() throws Exception {
        beforeCallCount++;
    }

    @Test
    public void shouldInitializeApplication() throws Exception {
        assertNotNull(Robolectric.application);
        assertEquals(CustomApplication.class, Robolectric.application.getClass());
    }

    @Test
    public void shouldInvokePrepareTestWithAnInstanceOfTheTest() throws Exception {
        assertEquals(this, preparedTest);
        assertEquals(RobolectricClassLoader.class.getName(), preparedTest.getClass().getClassLoader().getClass().getName());
    }

    @Test
    public void shouldInvokeBeforeTestWithTheCorrectMethod() throws Exception {
        assertEquals("shouldInvokeBeforeTestWithTheCorrectMethod", testMethod.getName());
    }

    @AfterClass
    public static void shouldHaveCalledAfterTest() {
        assertTrue(beforeCallCount > 0);
        assertEquals(beforeCallCount, afterTestCallCount);
    }

    public static class CustomConfig extends TestConfigs.WithDefaults {
        @Override
        public Class<? extends DefaultTestRun> getTestRunClass(FrameworkMethod method) {
            return CustomTestRun.class;
        }

        public static class CustomTestRun extends DefaultTestRun {
            public CustomTestRun(RobolectricContext robolectricContext, FrameworkMethod frameworkMethod) {
                super(robolectricContext, frameworkMethod);
            }

            @Override public void prepareTest(Object test) {
                ((CustomRobolectricTestRunnerTest) test).preparedTest = test;
            }

            @Override public void beforeTest(Method method) {
                testMethod = method;
            }

            @Override public void afterTest(Method method) {
                afterTestCallCount++;
            }

            @Override protected Application createApplication() {
                return new CustomApplication();
            }

        }
    }

    public static class CustomApplication extends Application {
    }
}
