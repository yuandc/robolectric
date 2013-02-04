package org.robolectric.shadows;

import android.app.Activity;
import android.content.Context;
import android.widget.AbsSeekBar;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class AbsSeekBarTest {
	
	@Test
	public void testInheritance() {
		TestAbsSeekBar seekBar = new TestAbsSeekBar(new Activity());
		ShadowAbsSeekBar shadow = Robolectric.shadowOf(seekBar);
		assertThat(shadow, instanceOf(ShadowProgressBar.class));
	}
	
	private static class TestAbsSeekBar extends AbsSeekBar {
		
		public TestAbsSeekBar(Context context) {
			super(context);
		}
	}
}
