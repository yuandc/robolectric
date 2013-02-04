package org.robolectric.shadows;

import android.view.InputDevice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class InputDeviceTest {
    @Test
    public void canConstructInputDeviceWithName() throws Exception {
        InputDevice inputDevice = ShadowInputDevice.makeInputDeviceNamed("foo");
        assertThat(inputDevice.getName(), equalTo("foo"));
    }
}
