package org.robolectric.shadows;

import android.view.InputDevice;
import android.view.KeyEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class KeyEventTest {
    @Test
    public void canSetInputDeviceOnKeyEvent() throws Exception {
        InputDevice myDevice = ShadowInputDevice.makeInputDeviceNamed("myDevice");
        KeyEvent keyEvent = new KeyEvent(1, 2);
        shadowOf(keyEvent).setDevice(myDevice);
        assertThat(keyEvent.getDevice().getName(), equalTo("myDevice"));
    }
}
