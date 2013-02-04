package org.robolectric.shadows;

import android.net.wifi.ScanResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class ScanResultTest {

    @Test
    public void shouldConstruct() throws Exception {
        ScanResult scanResult = ShadowScanResult.newInstance("SSID", "BSSID", "Caps", 11, 42);
        assertThat(scanResult.SSID, equalTo("SSID"));
        assertThat(scanResult.BSSID, equalTo("BSSID"));
        assertThat(scanResult.capabilities, equalTo("Caps"));
        assertThat(scanResult.level, equalTo(11));
        assertThat(scanResult.frequency, equalTo(42));
        assertNotNull(shadowOf(scanResult).realObject);
    }
}
