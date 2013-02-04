package org.robolectric.shadows;

import android.content.ContentProviderResult;
import android.net.Uri;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class ContentProviderResultTest {
    @Test
    public void count() {
        ContentProviderResult result = new ContentProviderResult(5);
        assertThat(result.count, is(5));
    }
    
    @Test
    public void uri() {
        Uri uri = Uri.parse("content://org.robolectric");
        ContentProviderResult result = new ContentProviderResult(uri);
        assertThat(result.uri, equalTo(uri));
    }
}