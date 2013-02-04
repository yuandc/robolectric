package org.robolectric.shadows;

import android.text.SpannedString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class SpannedStringTest {

    @Test
    public void toString_shouldDelegateToUnderlyingCharSequence() {
        SpannedString spannedString = new SpannedString("foo");
        assertEquals("foo", spannedString.toString());
    }

    @Test
    public void valueOfSpannedString_shouldReturnItself() {
        SpannedString spannedString = new SpannedString("foo");
        assertSame(spannedString, SpannedString.valueOf(spannedString));
    }

    @Test
    public void valueOfCharSequence_shouldReturnNewSpannedString() {
        assertEquals("foo", SpannedString.valueOf("foo").toString());
    }
    

}

