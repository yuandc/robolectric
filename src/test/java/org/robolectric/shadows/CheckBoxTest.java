package org.robolectric.shadows;

import android.widget.CheckBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class CheckBoxTest {
    @Test
    public void testWorks() throws Exception {
        CheckBox checkBox = new CheckBox(null);
        assertThat(checkBox.isChecked(), equalTo(false));

        checkBox.setChecked(true);
        assertThat(checkBox.isChecked(), equalTo(true));

        checkBox.performClick();
        assertThat(checkBox.isChecked(), equalTo(false));

        checkBox.toggle();
        assertThat(checkBox.isChecked(), equalTo(true));
    }
}
