package org.robolectric.shadows;

import android.graphics.CornerPathEffect;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static junit.framework.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;


@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class CornerPathEffectTest {
    @Test
    public void shouldGetRadius() throws Exception {
        CornerPathEffect cornerPathEffect = new CornerPathEffect(4.0f);
        assertEquals(4.0f, shadowOf(cornerPathEffect).getRadius());
    }
}
