package org.robolectric.shadows;

import android.app.Activity;
import android.app.Dialog;
import android.preference.PreferenceScreen;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PreferenceScreenTest {

	private PreferenceScreen screen;
	private ShadowPreferenceScreen shadow;

    @Before
    public void setUp() throws Exception {
    	screen = Robolectric.newInstanceOf(PreferenceScreen.class);
    	shadow = Robolectric.shadowOf(screen);
    }
    
	@Test
	public void shouldInheritFromPreferenceGroup() {
		assertThat(shadow, instanceOf(ShadowPreferenceGroup.class));
	}
	
	@Test
	public void shouldSetDialog() {
		Dialog dialog = new Dialog(new Activity());
		
		assertThat(screen.getDialog(), nullValue());
		shadow.setDialog(dialog);
		assertThat(screen.getDialog(), sameInstance(dialog));		
	}
}
