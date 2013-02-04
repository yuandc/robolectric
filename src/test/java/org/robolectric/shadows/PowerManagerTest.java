package org.robolectric.shadows;

import android.content.Context;
import android.os.PowerManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PowerManagerTest {

    PowerManager powerManager;
    ShadowPowerManager shadowPowerManager;

    @Before
    public void before() {
        powerManager = (PowerManager) Robolectric.application.getSystemService(Context.POWER_SERVICE);
        shadowPowerManager = Robolectric.shadowOf(powerManager);
    }

    @Test
    public void testIsScreenOn() {
        assertTrue(powerManager.isScreenOn());
        shadowPowerManager.setIsScreenOn(false);
        assertFalse(powerManager.isScreenOn());
    }

    @Test
    public void shouldCreateWakeLock() throws Exception {
        assertNotNull(powerManager.newWakeLock(0, "TAG"));
    }

    @Test
    public void shouldAcquireAndReleaseReferenceCountedLock() throws Exception {
        PowerManager.WakeLock lock = powerManager.newWakeLock(0, "TAG");
        assertFalse(lock.isHeld());
        lock.acquire();
        assertTrue(lock.isHeld());
        lock.acquire();

        assertTrue(lock.isHeld());
        lock.release();

        assertTrue(lock.isHeld());
        lock.release();
        assertFalse(lock.isHeld());
    }

    @Test
    public void shouldAcquireAndReleaseNonReferenceCountedLock() throws Exception {
        PowerManager.WakeLock lock = powerManager.newWakeLock(0, "TAG");
        lock.setReferenceCounted(false);

        assertFalse(lock.isHeld());
        lock.acquire();
        assertTrue(lock.isHeld());
        lock.acquire();
        assertTrue(lock.isHeld());

        lock.release();

        assertFalse(lock.isHeld());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntimeExceptionIfLockisUnderlocked() throws Exception {
        PowerManager.WakeLock lock = powerManager.newWakeLock(0, "TAG");
        lock.release();
    }
    
    @Test
    public void shouldLogLatestWakeLock() throws Exception {
    	ShadowPowerManager.reset();
    	assertThat( shadowPowerManager.getLatestWakeLock(), nullValue() );

    	PowerManager.WakeLock lock = powerManager.newWakeLock(0, "TAG");
    	lock.acquire();

    	assertThat( shadowPowerManager.getLatestWakeLock(), notNullValue() );
    	assertThat( shadowPowerManager.getLatestWakeLock(), sameInstance( lock ) );
    	assertThat( lock.isHeld(), equalTo(true) );
    	
    	lock.release();
    	
    	assertThat( shadowPowerManager.getLatestWakeLock(), notNullValue() );
    	assertThat( shadowPowerManager.getLatestWakeLock(), sameInstance( lock ) );
    	assertThat( lock.isHeld(), equalTo(false) );
    	
    	ShadowPowerManager.reset();
    	assertThat( shadowPowerManager.getLatestWakeLock(), nullValue() );
    }
}