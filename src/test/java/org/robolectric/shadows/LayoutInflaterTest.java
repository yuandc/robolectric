package org.robolectric.shadows;

import android.content.ContextWrapper;
import android.view.LayoutInflater;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class LayoutInflaterTest {
    private LayoutInflater layoutInflater;

    @Before
    public void setUp() throws Exception {
        layoutInflater = LayoutInflater.from(Robolectric.application);
    }

    @Test
    public void getInstance_shouldReturnSameInstance() throws Exception {
        assertNotNull(layoutInflater);
        assertSame(LayoutInflater.from(Robolectric.application), layoutInflater);
        assertSame(LayoutInflater.from(new ContextWrapper(Robolectric.application)), layoutInflater);
    }
}
