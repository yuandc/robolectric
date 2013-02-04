package org.robolectric.shadows;

import android.nfc.NdefRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class NdefRecordTest {

    @Test
    public void getPayload() throws Exception {
        byte[] bytes = "mumble".getBytes();
        NdefRecord ndefRecord = new NdefRecord(bytes);

        assertSame(ndefRecord.getPayload(), bytes);
    }
}
