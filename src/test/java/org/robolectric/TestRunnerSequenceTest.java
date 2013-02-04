package org.robolectric;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.robolectric.util.Transcript;

import java.lang.reflect.Method;

public class TestRunnerSequenceTest {
    public static Transcript transcript = new Transcript();

    @Test public void shouldRunThingsInTheRightOrder() throws Exception {
        transcript.assertEventsSoFar(
                "configureShadows",
                "resetStaticState",
                "setupApplicationState",
                "beforeTest"
        );
    }

    public static class Runner extends RobolectricContext {
        @Override
        public Class<? extends DefaultTestRun> getTestRunClass(FrameworkMethod method) {
            return MyTestRun.class;
        }

        public static class MyTestRun extends DefaultTestRun {
            public MyTestRun(RobolectricContext robolectricContext, FrameworkMethod frameworkMethod) {
                super(robolectricContext, frameworkMethod);
            }

            @Override public void beforeTest(Method method) {
                transcript.add("beforeTest");
            }

            @Override protected void resetStaticState() {
                transcript.add("resetStaticState");
            }

            @Override protected void configureShadows(Method testMethod) {
                transcript.add("configureShadows");
            }

            @Override
            public void setupApplicationState(Method testMethod) {
                transcript.add("setupApplicationState");
            }
        }
    }
}
