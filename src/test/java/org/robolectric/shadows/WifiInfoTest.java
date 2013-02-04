package org.robolectric.shadows;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static android.content.Context.WIFI_SERVICE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class WifiInfoTest {

    @Test
    public void shouldReturnMacAddress() {
        WifiManager wifiManager = (WifiManager) application.getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        shadowOf(wifiInfo).setMacAddress("mac address");

        wifiManager = (WifiManager) application.getSystemService(WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        assertThat(wifiInfo.getMacAddress(), equalTo("mac address"));
    }
}
