package org.robolectric.shadows;

import android.text.method.LinkMovementMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class LinkMovementMethodTest {

    @Test
    public void getInstance_shouldReturnAnInstanceOf_LinkedMovementMethod() throws Exception {
        assertThat(LinkMovementMethod.getInstance(), instanceOf(LinkMovementMethod.class));
    }

}
