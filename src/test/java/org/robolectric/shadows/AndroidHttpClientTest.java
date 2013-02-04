package org.robolectric.shadows;

import android.net.http.AndroidHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.util.Strings;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class AndroidHttpClientTest {

    @Test
    public void testNewInstance() throws Exception {
        AndroidHttpClient client = AndroidHttpClient.newInstance("foo");
        assertThat(client, not(nullValue()));
    }

    @Test
    public void testNewInstanceWithContext() throws Exception {
        AndroidHttpClient client = AndroidHttpClient.newInstance("foo", Robolectric.application);
        assertThat(client, not(nullValue()));
    }

    @Test
    public void testExecute() throws IOException {
        AndroidHttpClient client = AndroidHttpClient.newInstance("foo");
        Robolectric.addPendingHttpResponse(200, "foo");
        HttpResponse resp = client.execute(new HttpGet("/foo"));
        assertThat(resp.getStatusLine().getStatusCode(), is(200));
        assertThat(Strings.fromStream(resp.getEntity().getContent()), equalTo("foo"));
    }
}
