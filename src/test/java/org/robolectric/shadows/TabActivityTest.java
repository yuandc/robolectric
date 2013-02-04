package org.robolectric.shadows;

import android.app.TabActivity;
import android.widget.TabHost;
import android.widget.TabWidget;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class TabActivityTest {

    @Test
    public void tabActivityShouldNotMakeNewTabHostEveryGet() throws Exception {
        TabActivity activity = new TabActivity();
        TabHost tabHost1 = activity.getTabHost();
        TabHost tabHost2 = activity.getTabHost();

        assertThat(tabHost1, equalTo(tabHost2));
    }

    @Test
    public void shouldGetTabWidget() throws Exception {
        TabActivity activity = new TabActivity();
        activity.setContentView(R.layout.tab_activity);
        assertThat(activity.getTabWidget(), instanceOf(TabWidget.class));
    }
}
