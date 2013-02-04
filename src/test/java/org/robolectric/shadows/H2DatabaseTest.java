package org.robolectric.shadows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.util.DatabaseConfig;
import org.robolectric.util.H2Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@DatabaseConfig.UsingDatabaseMap(H2Map.class)
@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class H2DatabaseTest extends DatabaseTestBase {
    @Test
    public void shouldUseH2DatabaseMap() throws Exception {
        assertThat(DatabaseConfig.getDatabaseMap().getClass().getName(), 
                equalTo(H2Map.class.getName()));
    }
}
