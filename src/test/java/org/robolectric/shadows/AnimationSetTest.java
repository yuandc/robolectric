package org.robolectric.shadows;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class AnimationSetTest {
	private AnimationSet set;
	private ShadowAnimationSet shadow;
	
	@Before
	public void setUp() {
		set = new AnimationSet(true);
		shadow = shadowOf(set);
	}
	
	
	@Test
	public void testAnimationList() {
		Animation alpha = new AlphaAnimation(1f, 2f);
		Animation translate =  new TranslateAnimation(1f, 2f, 3f, 4f);
		Animation rotate = new RotateAnimation(1f, 2f);
		set.addAnimation(alpha);
		set.addAnimation(translate);
		set.addAnimation(rotate);
		
		List<Animation> list = shadow.getAnimations();
		assertThat(list.size(), equalTo(3));
		assertThat(list.get(0), sameInstance(alpha));
		assertThat(list.get(1), sameInstance(translate));
		assertThat(list.get(2), sameInstance(rotate));
	}

}
