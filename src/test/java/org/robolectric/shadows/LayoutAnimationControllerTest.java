package org.robolectric.shadows;

import android.app.Activity;
import android.view.animation.LayoutAnimationController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class LayoutAnimationControllerTest {
	private ShadowLayoutAnimationController shadow;
	
	@Before
	public void setup() {
		LayoutAnimationController controller = new LayoutAnimationController(new Activity(), null);
		shadow = Robolectric.shadowOf(controller);
	}
	
	@Test
	public void testResourceId() {
		int id = 1;
		shadow.setLoadedFromResourceId(1);
		assertThat(shadow.getLoadedFromResourceId(), equalTo(id));
	}

}
