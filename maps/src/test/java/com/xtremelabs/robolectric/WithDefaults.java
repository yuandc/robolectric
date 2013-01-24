package com.xtremelabs.robolectric;

import org.junit.runners.model.InitializationError;

import java.io.File;

public class WithDefaults extends RobolectricTestRunner {
    public static File testDirLocation;

    public WithDefaults(Class<?> testClass) throws InitializationError {
        super(RobolectricContext.bootstrap(WithDefaults.class, testClass, new RobolectricContext.Factory() {
            @Override
            public RobolectricContext create() {
                return new RobolectricContext() {
                    @Override
                    protected AndroidManifest createAppManifest() {
                        return new AndroidManifest(resourceFile("TestAndroidManifest.xml"), resourceFile("res"), resourceFile("assets"));
                    }
                };
            }
        }));
    }

    public static File file(String... pathParts) {
        return file(new File("."), pathParts);
    }

    public static File file(File f, String... pathParts) {
        for (String pathPart : pathParts) {
            f = new File(f, pathPart);
        }
        return f;
    }

    public static File resourceFile(String... pathParts) {
        return file(resourcesBaseDir(), pathParts);
    }

    public static File resourcesBaseDir() {
        if (testDirLocation == null) {
            File testDir = file("src", "test", "resources");
            if (hasTestManifest(testDir)) return testDirLocation = testDir;

            File roboTestDir = file("robolectric", "src", "test", "resources");
            if (hasTestManifest(roboTestDir)) return testDirLocation = roboTestDir;

            File submoduleDir = file("submodules", "robolectric", "src", "test", "resources");
            if (hasTestManifest(submoduleDir)) return testDirLocation = submoduleDir;

            //required for robolectric-sqlite to find resources to test against
            File roboSiblingTestDir = file(new File(new File(".").getAbsolutePath()).getParentFile().getParentFile(),"robolectric", "src", "test", "resources");
            if (hasTestManifest(roboSiblingTestDir)) return testDirLocation = roboSiblingTestDir;

            throw new RuntimeException("can't find your TestAndroidManifest.xml in "
                    + testDir.getAbsolutePath() + " or " + roboTestDir.getAbsolutePath() + "\n or " + roboSiblingTestDir.getAbsolutePath());
        } else {
            return testDirLocation;
        }
    }

    private static boolean hasTestManifest(File testDir) {
        return new File(testDir, "TestAndroidManifest.xml").isFile();
    }
}
