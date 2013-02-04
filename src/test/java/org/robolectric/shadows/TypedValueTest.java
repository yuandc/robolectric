package org.robolectric.shadows;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class TypedValueTest {

    @Test
    public void testApplyDimensionIsWired() throws Exception {
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.density = 0.5f;
        float convertedValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, metrics);
        assertThat(convertedValue, equalTo(50f));
    }
}
