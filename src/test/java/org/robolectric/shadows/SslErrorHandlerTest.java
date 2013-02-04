package org.robolectric.shadows;

import android.webkit.SslErrorHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class SslErrorHandlerTest {

    private SslErrorHandler handler;
    private ShadowSslErrorHandler shadow;

    @Before
    public void setUp() throws Exception {
        handler = Robolectric.newInstanceOf(SslErrorHandler.class);
        shadow = Robolectric.shadowOf(handler);
    }

    @Test
    public void shouldInheritFromShadowHandler() {
        assertThat(shadow, instanceOf(ShadowHandler.class));
    }

    @Test
    public void shouldRecordCancel() {
        assertThat(shadow.wasCancelCalled(), equalTo(false));
        handler.cancel();
        assertThat(shadow.wasCancelCalled(), equalTo(true));
    }

    @Test
    public void shouldRecordProceed() {
        assertThat(shadow.wasProceedCalled(), equalTo(false));
        handler.proceed();
        assertThat(shadow.wasProceedCalled(), equalTo(true));
    }
}
