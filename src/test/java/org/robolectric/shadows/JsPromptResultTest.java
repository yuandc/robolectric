package org.robolectric.shadows;

import android.webkit.JsPromptResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class JsPromptResultTest {

    @Test
    public void shouldConstruct() throws Exception {
        JsPromptResult result = ShadowJsPromptResult.newInstance();
        assertNotNull(result);
    }
}
