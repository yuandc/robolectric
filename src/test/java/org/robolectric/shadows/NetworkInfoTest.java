package org.robolectric.shadows;

import android.net.NetworkInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class NetworkInfoTest {

    @Test
    public void getDetailedState_shouldReturnTheAssignedState() throws Exception {
        NetworkInfo networkInfo = Robolectric.newInstanceOf(NetworkInfo.class);
        shadowOf(networkInfo).setDetailedState(NetworkInfo.DetailedState.SCANNING);
        assertThat(networkInfo.getDetailedState(), equalTo(NetworkInfo.DetailedState.SCANNING));
    }
}
