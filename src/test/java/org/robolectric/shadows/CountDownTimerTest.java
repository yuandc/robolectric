package org.robolectric.shadows;

import android.os.CountDownTimer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class CountDownTimerTest {
	
	private ShadowCountDownTimer shadowCountDownTimer;
	private CountDownTimer countDownTimer;
	private long millisInFuture = 2000;
	private long countDownInterval = 1000;
	private String msg = null;
	
    @Before
    public void setUp() throws Exception {
    	
    	countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
    		
			@Override
			public void onFinish() {
				msg = "onFinish() is called";
			}

			@Override
			public void onTick(long millisUnitilFinished) {
				msg = "onTick() is called";
			}    		
    	};
    	shadowCountDownTimer = Robolectric.shadowOf(countDownTimer);
    }
	
	
	@Test
	public void testInvokeOnTick() {
		assertThat(msg, not(equalTo("onTick() is called")));
		shadowCountDownTimer.invokeTick(countDownInterval);
		assertThat(msg, equalTo("onTick() is called"));		
	}
	
	@Test
	public void testInvokeOnFinish() {
		assertThat(msg, not(equalTo("onFinish() is called")));
		shadowCountDownTimer.invokeFinish();
		assertThat(msg, equalTo("onFinish() is called"));
	}
	
	@Test
	public void testStart() {
		assertThat(shadowCountDownTimer.hasStarted(), equalTo(false));
		CountDownTimer timer = shadowCountDownTimer.start();
		assertThat(timer, notNullValue());
		assertThat(shadowCountDownTimer.hasStarted(), equalTo(true));
	}
	
	@Test
	public void testCancel() {
		CountDownTimer timer = shadowCountDownTimer.start();
		assertThat(timer, notNullValue());
		assertThat(shadowCountDownTimer.hasStarted(), equalTo(true));
		shadowCountDownTimer.cancel();
		assertThat(shadowCountDownTimer.hasStarted(), equalTo(false));			
	}
	
	@Test
	public void testAccessors() {
		assertThat(shadowCountDownTimer.getCountDownInterval(), equalTo(countDownInterval));
		assertThat(shadowCountDownTimer.getMillisInFuture(), equalTo(millisInFuture));
	}
}
