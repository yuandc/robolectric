package org.robolectric.shadows;

import android.app.Activity;
import android.widget.Spinner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class SpinnerTest {

    private Spinner spinner;

    @Before
    public void beforeTests() {
        spinner = new Spinner(new Activity());
    }

    @Test
    public void testPrompt() {
        spinner.setPrompt("foo");

        assertThat(spinner.getPrompt().toString(), is("foo"));
    }
}
