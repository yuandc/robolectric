package org.robolectric.shadows;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteCallbackList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class RemoteCallbackListTest {
    @Test
    public void testBasicWiring() throws Exception {
        RemoteCallbackList<Foo> fooRemoteCallbackList = new RemoteCallbackList<Foo>();
        Foo callback = new Foo();
        fooRemoteCallbackList.register(callback);

        fooRemoteCallbackList.beginBroadcast();

        assertThat(fooRemoteCallbackList.getBroadcastItem(0), sameInstance(callback));
    }

    public static class Foo implements IInterface {

        @Override
        public IBinder asBinder() {
            return new Binder();
        }
    }
}