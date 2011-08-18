package com.xtremelabs.robolectric;


import com.xtremelabs.robolectric.bytecode.RobolectricClassLoader;
import com.xtremelabs.robolectric.bytecode.ShadowWrangler;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;

import static com.xtremelabs.robolectric.util.TestUtil.resourceFile;

public class WithCustomClassListTestRunner extends RobolectricTestRunner {

	public WithCustomClassListTestRunner(@SuppressWarnings("rawtypes") Class testClass) throws InitializationError {
		this(testClass, new RobolectricConfig(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets")));
	}

	public WithCustomClassListTestRunner(Class<?> testClass, RobolectricConfig robolectricConfig) throws InitializationError {
			super(testClass,
				ShadowWrangler.getInstance(),
				isInstrumented() ? null : getClassLoader(),
				isInstrumented() ? null : robolectricConfig);
	}

    private static RobolectricClassLoader getClassLoader() {
        if (USE_REAL_ANDROID_SOURCES) {
            return new RobolectricClassLoader(getRealAndroidSourcesClassLoader(), ShadowWrangler.getInstance(), populateList());
        } else {
            return new RobolectricClassLoader(RobolectricClassLoader.class.getClassLoader(), ShadowWrangler.getInstance(), populateList());
        }
    }

    private static ArrayList<String> populateList() {
		ArrayList<String> testList = new ArrayList<String>();
		testList.add("com.xtremelabs.robolectric.bytecode.AndroidTranslatorClassIntrumentedTest$CustomPaint");
		return testList;
	}
}
