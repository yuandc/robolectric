package org.robolectric.shadows;

import android.content.Context;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;
import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.internal.RealObject;

import static org.robolectric.Robolectric.directlyOn;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(value = ContextThemeWrapper.class, inheritImplementationMethods = true)
public class ShadowContextThemeWrapper extends ShadowContextWrapper {
    @RealObject private ContextThemeWrapper realContextThemeWrapper;

//    @Implementation
//    @Override public Resources.Theme getTheme() {
//        return super.getTheme();
//    }

    @Override public void callAttachBaseContext(Context context) {
        directlyOn(realContextThemeWrapper, ContextThemeWrapper.class, "attachBaseContext", Context.class).invoke(context);
    }
}