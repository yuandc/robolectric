package android.content;

import com.xtremelabs.robolectric.internal.DoNotInstrument;

@DoNotInstrument
public class TestIntentSender extends IntentSender {
    public Intent intent;
}
