package org.robolectric.res;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.util.TestUtil;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.robolectric.util.TestUtil.resourceFile;
import static org.robolectric.util.TestUtil.testResources;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class RawResourceLoaderTest {

    private RawResourceLoader rawResourceLoader;

    @Before public void setUp() throws Exception {
        rawResourceLoader = new RawResourceLoader(new ResourceExtractor(testResources()), resourceFile("res"));
    }

    @Test
    public void shouldReturnRawResourcesWithExtensions() throws Exception {
        InputStream is = rawResourceLoader.getValue(R.raw.raw_resource);
        assertEquals("raw txt file contents", TestUtil.readString(is));
    }

    @Test
    public void shouldReturnRawResourcesWithoutExtensions() throws Exception {
        InputStream is = rawResourceLoader.getValue(R.raw.raw_no_ext);
        assertEquals("no ext file contents", TestUtil.readString(is));
    }
}
