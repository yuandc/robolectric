package org.robolectric.shadows;

import android.widget.ImageButton;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.res.PackageResourceLoader;
import org.robolectric.tester.android.util.Attribute;
import org.robolectric.tester.android.util.TestAttributeSet;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class ImageButtonTest {
    @Test
    public void testBackground() throws Exception {
        TestAttributeSet attrs = new TestAttributeSet(new ArrayList<Attribute>(), new PackageResourceLoader(), null);
        ImageButton button = new ImageButton(Robolectric.application, attrs);
        assertThat(button.getBackground(), notNullValue());
    }
}
