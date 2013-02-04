package org.robolectric.shadows;

import android.telephony.PhoneNumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PhoneNumberUtilTest {
    @Test
    public void testFormatNumber() {
        assertThat(PhoneNumberUtils.formatNumber("12345678901"), equalTo("12345678901-formatted"));
    }

    @Test
    public void testStripSeparators() {
        assertThat(PhoneNumberUtils.stripSeparators("12345678901"), equalTo("12345678901-stripped"));
    }
        
}
