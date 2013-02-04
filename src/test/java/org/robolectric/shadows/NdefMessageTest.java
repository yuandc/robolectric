package org.robolectric.shadows;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class NdefMessageTest {

    @Test
    public void getRecords() throws Exception {
        NdefRecord[] ndefRecords = {new NdefRecord("mumble".getBytes())};
        NdefMessage ndefMessage = new NdefMessage(ndefRecords);

        assertSame(ndefMessage.getRecords(), ndefRecords);
    }
}