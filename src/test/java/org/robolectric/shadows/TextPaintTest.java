package org.robolectric.shadows;

import android.text.TextPaint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static junit.framework.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class TextPaintTest {
    
    @Test
    public void measureText_returnsStringLengthAsMeasurement() throws Exception {
        TextPaint paint = new TextPaint();
        assertEquals(4f, paint.measureText("1234"));
    }
}
