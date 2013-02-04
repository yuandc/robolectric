package org.robolectric.shadows;


import android.content.res.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class ConfigurationTest {

    private Configuration configuration;
    private ShadowConfiguration shConfiguration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration();
        shConfiguration = Robolectric.shadowOf( configuration );
    }

    @Test
    public void testSetToDefaults() throws Exception {
        configuration.setToDefaults();
        assertThat(configuration.screenLayout, equalTo(Configuration.SCREENLAYOUT_LONG_NO | Configuration.SCREENLAYOUT_SIZE_NORMAL));
    }
    
    @Test
    public void testSetLocale() {
    	shConfiguration.setLocale( Locale.US );
    	assertThat( configuration.locale, equalTo( Locale.US ) );

    	shConfiguration.setLocale( Locale.FRANCE);
    	assertThat( configuration.locale, equalTo( Locale.FRANCE ) );
    }

    @Test
    public void testConstructCopy() {
        configuration.setToDefaults();
        Configuration clone = new Configuration(configuration);
        assertThat( configuration, equalTo( clone ) );
    }

}
