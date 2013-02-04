package org.robolectric.shadows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class DatabaseUtilsTest {

	@Test
	public void testQuote() {
		assertThat( ShadowDatabaseUtils.sqlEscapeString( "foobar" ), equalTo( "'foobar'" ) );
		assertThat( ShadowDatabaseUtils.sqlEscapeString( "Rich's" ), equalTo( "'Rich''s'" ) );
	}
}
