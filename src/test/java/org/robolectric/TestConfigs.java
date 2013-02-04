package org.robolectric;

import javassist.CtClass;
import org.robolectric.bytecode.AndroidTranslatorClassInstrumentedTest;
import org.robolectric.bytecode.Setup;

import java.lang.reflect.Method;

import static org.robolectric.util.TestUtil.resourceFile;

public class TestConfigs {
    public static class WithCustomClassList extends RobolectricContext {
        @Override
        protected AndroidManifest createAppManifest() {
            return new AndroidManifest(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets"));
        }

        @Override
        public Setup createSetup() {
            return new Setup() {
                @Override
                public boolean shouldInstrument(CtClass ctClass) {
                    String name = ctClass.getName();
                    if (name.equals(AndroidTranslatorClassInstrumentedTest.CustomPaint.class.getName())
                            || name.equals(AndroidTranslatorClassInstrumentedTest.ClassWithPrivateConstructor.class.getName())) {
                        return true;
                    }
                    return super.shouldInstrument(ctClass);
                }
            };
        }
    }

    public static class WithoutDefaults extends RobolectricContext {
//        @Override
        protected void configureShadows(Method testMethod) {
            // Don't do any class binding, because that's what we're trying to test here.
        }

//        @Override
        public void setupApplicationState(Method testMethod) {
            // Don't do any resource loading or app init, because that's what we're trying to test here.
        }
    }

    public static class WithDefaults extends RobolectricContext {
        @Override
        protected AndroidManifest createAppManifest() {
            return new AndroidManifest(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets"));
        }
    }

    public static class RealApisWithDefaults extends RobolectricContext {
        @Override
        protected AndroidManifest createAppManifest() {
            return new AndroidManifest(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets"));
        }

        @Override
        public Setup createSetup() {
            return new Setup() {
                @Override
                public boolean invokeApiMethodBodiesWhenShadowMethodIsMissing(Class clazz, String methodName, Class<?>[] paramClasses) {
                    return true;
                }
            };
        }
    }

    public static class RealApisWithoutDefaults extends RobolectricContext {
        @Override
        public Setup createSetup() {
            return new Setup() {
                @Override
                public boolean invokeApiMethodBodiesWhenShadowMethodIsMissing(Class clazz, String methodName, Class<?>[] paramClasses) {
                    return true;
                }
            };
        }

//        @Override
        protected void configureShadows(Method testMethod) {
            // Don't do any class binding, because that's what we're trying to test here.
        }

//        @Override
        public void setupApplicationState(Method testMethod) {
            // Don't do any resource loading or app init, because that's what we're trying to test here.
        }
    }
}
