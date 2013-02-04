package org.robolectric.shadows;

import android.graphics.Paint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;


@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PaintTest {

	@Test
	public void shouldGetIsDitherInfo() {
		Paint paint = Robolectric.newInstanceOf(Paint.class);
		assertFalse(paint.isAntiAlias());
		ShadowPaint shadowPaint = shadowOf(paint);
		shadowPaint.setAntiAlias(true);
		assertTrue(paint.isAntiAlias());		
	}
	
	@Test
	public void shouldGetIsAntiAlias() {
		Paint paint = Robolectric.newInstanceOf(Paint.class);
		assertFalse(paint.isAntiAlias());
		ShadowPaint shadowPaint = shadowOf(paint);
		shadowPaint.setAntiAlias(true);
		assertTrue(paint.isAntiAlias());				
	}
	
	@Test
	public void testCtor() {
		Paint paint = Robolectric.newInstanceOf(Paint.class);
		assertFalse(paint.isAntiAlias());
		ShadowPaint shadowPaint = shadowOf(paint);
		shadowPaint.__constructor__( Paint.ANTI_ALIAS_FLAG );
		assertTrue(paint.isAntiAlias());		
	}
}
