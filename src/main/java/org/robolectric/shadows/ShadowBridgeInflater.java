package org.robolectric.shadows;

import android.app.Application;
import android.content.Context;
import android.view.BridgeInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.layoutlib.bridge.android.BridgeContext;
import org.fest.reflect.field.Invoker;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.internal.RealObject;

import static org.fest.reflect.core.Reflection.field;
import static org.robolectric.Robolectric.directlyOn;

@Implements(BridgeInflater.class)
public class ShadowBridgeInflater extends ShadowLayoutInflater {
    @RealObject private BridgeInflater realBridgeInflater;

    // arghghgh, BridgeInflater.inflate() always returns null if context isn't BridgeContext arghghghg
    @Implementation
    public View inflate(int resource, ViewGroup root) {
        Invoker<Context> mContextField = field("mContext").ofType(Context.class).in(realBridgeInflater);
        Context originalContext = mContextField.get();
        Context context = originalContext;
        if (!(context instanceof BridgeContext)) {
            context = context.getApplicationContext();
            if (!(context instanceof BridgeContext)) {
                context = ((Application) context).getBaseContext();
            }
        }
        if (!(context instanceof BridgeContext)) {
            throw new RuntimeException("can't find a BridgeContext for " + originalContext);
        }
        mContextField.set(context);
        try {
            return directlyOn(realBridgeInflater, BridgeInflater.class).inflate(resource, root);
        } finally {
            mContextField.set(originalContext);
        }
    }

    // why do you hate us, BridgeInflater?
    @Implementation
    public LayoutInflater cloneInContext(Context newContext) {
        BridgeInflater bridgeInflater = (BridgeInflater) directlyOn(realBridgeInflater, BridgeInflater.class).cloneInContext(newContext);
        field("mProjectCallback").ofType(IProjectCallback.class).in(bridgeInflater).set(
                field("mProjectCallback").ofType(IProjectCallback.class).in(realBridgeInflater).get()
        );
        return bridgeInflater;
    }
}
