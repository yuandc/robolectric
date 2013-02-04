package org.robolectric.shadows;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static junit.framework.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PagerAdapterTest {
    @Test
    public void shouldNotifyDataSetChanged() throws Exception {
        PagerAdapter pagerAdapter = new NullPagerAdapter();
        TestDataSetObserver datasetObserver= new TestDataSetObserver();
        shadowOf(pagerAdapter).registerDataSetObserver(datasetObserver);
        pagerAdapter.notifyDataSetChanged();

        assertTrue(datasetObserver.onChangedWasCalled);
    }

    private static class NullPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }

    private class TestDataSetObserver extends DataSetObserver {
        boolean onChangedWasCalled;
        @Override
        public void onChanged() {
            onChangedWasCalled = true;
        }
    }
}
