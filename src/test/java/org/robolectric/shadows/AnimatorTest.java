package org.robolectric.shadows;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricShadowOfLevel16;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class AnimatorTest {
    @Test
    public void shouldBeAbleToNotifyListenersOfStartAndEnd() throws Exception {
        Animator animator = new StubAnimator();
        TestAnimatorListener startListener = new TestAnimatorListener();
        TestAnimatorListener endListener = new TestAnimatorListener();
        animator.addListener(startListener);
        animator.addListener(endListener);

        RobolectricShadowOfLevel16.shadowOf(animator).notifyStart();
        assertThat(startListener.startWasCalled, equalTo(true));

        RobolectricShadowOfLevel16.shadowOf(animator).notifyEnd();
        assertThat(endListener.endWasCalled, equalTo(true));
    }

    private static class StubAnimator extends Animator {
        @Override
        public long getStartDelay() {
            return 0;
        }

        @Override
        public void setStartDelay(long startDelay) {
        }

        @Override
        public Animator setDuration(long duration) {
            return null;
        }

        @Override
        public long getDuration() {
            return 0;
        }

        @Override
        public void setInterpolator(TimeInterpolator value) {
        }

        @Override
        public boolean isRunning() {
            return false;
        }
    }
}
