package org.robolectric.shadows;

import android.os.Environment;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import java.io.File;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class EnvironmentTest {

	@After
	public void tearDown() throws Exception {
		deleteDir(ShadowContext.EXTERNAL_CACHE_DIR);
		deleteDir(ShadowContext.EXTERNAL_FILES_DIR);
		ShadowEnvironment.setExternalStorageState("removed");
	}
	
	@Test
	public void testExternalStorageState() {
		assertThat( Environment.getExternalStorageState(), equalTo("removed") );
		ShadowEnvironment.setExternalStorageState("mounted");
		assertThat( Environment.getExternalStorageState(), equalTo("mounted") );
	}
	
	@Test
	public void testGetExternalStorageDirectory() {
		 assertTrue(Environment.getExternalStorageDirectory().exists());
	}
	
	@Test
	public void testGetExternalStoragePublicDirectory() {
		File extStoragePublic = Environment.getExternalStoragePublicDirectory("Movies"); 
		assertTrue(extStoragePublic.exists());
		assertThat(extStoragePublic, equalTo( new File(ShadowContext.EXTERNAL_FILES_DIR, "Movies" ) ) );
	}
	
    public void deleteDir(File path) {
		if (path.isDirectory()) {
			File[] files = path.listFiles();
			for (File f : files) {
				deleteDir(f);
			}
		}
		path.delete();
	}

}
