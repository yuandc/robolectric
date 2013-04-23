package org.robolectric.shadows;

import android.content.res.Resources;
import com.android.layoutlib.bridge.android.BridgeContext;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.internal.RealObject;

import static org.robolectric.Robolectric.directlyOn;

@Implements(BridgeContext.class)
public class ShadowBridgeContext extends ShadowContext {
    @RealObject BridgeContext bridgeContext;

//    @Implementation
//    @Override public Resources.Theme getTheme() {
//        return directlyOn(bridgeContext, BridgeContext.class).getTheme();
//    }

    @Implementation
    @Override public Resources getResources() {
        return super.getResources();
    }
}
