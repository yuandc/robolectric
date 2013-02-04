package org.robolectric.shadows;

import android.nfc.NfcAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class NfcAdapterTest {
    @Test
    public void getDefaultAdapter_shouldReturnAnAdapter() throws Exception {
        assertThat(NfcAdapter.getDefaultAdapter(null), instanceOf(NfcAdapter.class));
    }
}
