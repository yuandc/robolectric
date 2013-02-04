package org.robolectric.shadows;

import android.app.Activity;
import android.preference.PreferenceCategory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PreferenceCategoryTest {

	private PreferenceCategory category;
	private ShadowPreferenceCategory shadow;

    @Before
    public void setUp() throws Exception {
    	category = new PreferenceCategory(new Activity());
    	shadow = Robolectric.shadowOf(category);
    }
    
	@Test
	public void shouldInheritFromPreferenceGroup() {
		assertThat(shadow, instanceOf(ShadowPreferenceGroup.class));
	}	
}
