package org.robolectric.shadows;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class FragmentTest {
    private Fragment fragment;
    private FragmentActivity fragmentActivity;

    @Before
    public void setUp() throws Exception {
        fragmentActivity = new FragmentActivity();
        fragment = new TestFragment();
        fragmentActivity.getSupportFragmentManager().beginTransaction().add(fragment, null).commit();
    }

    @Test
    public void retrieveIdOfResource() {
        int id = fragment.getResources().getIdentifier("hello", "string", "org.robolectric");
        assertTrue(id > 0);

        String hello = fragment.getString(id);
        assertEquals("Hello", hello);
    }

    @Test
    public void getString_returnsStringResource() throws Exception {
        assertEquals("Howdy", fragment.getString(R.string.howdy));
    }

    @Test(expected = IllegalStateException.class)
    public void unattachedFragmentsCannotGetResources() throws Exception {
        new TestFragment().getResources();
    }

    @Test(expected = IllegalStateException.class)
    public void unattachedFragmentsCannotGetStrings() throws Exception {
        new TestFragment().getString(R.string.howdy);
    }
}
