package org.robolectric.shadows;

import android.widget.ScrollView;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class ScrollViewTest {
    @Test
    public void shouldSmoothScrollTo() throws Exception {
        ScrollView scrollView = new ScrollView(null);
        scrollView.smoothScrollTo(7, 6);

        assertEquals(7, scrollView.getScrollX());
        assertEquals(6, scrollView.getScrollY());
    }
}
