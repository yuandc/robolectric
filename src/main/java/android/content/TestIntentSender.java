package android.content;

public class TestIntentSender extends IntentSender {
    public Intent intent;

    public TestIntentSender(IIntentSender target) {
        super(target);
    }
}
