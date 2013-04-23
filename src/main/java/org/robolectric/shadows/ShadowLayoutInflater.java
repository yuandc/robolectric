package org.robolectric.shadows;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import org.robolectric.internal.AppSingletonizer;
import org.robolectric.internal.Implements;

/**
 * Shadow of {@code LayoutInflater} that actually inflates layouts into {@code View}s that are functional enough to
 * support testing.
 */

@Implements(LayoutInflater.class)
public class ShadowLayoutInflater {
    private static AppSingletonizer<LayoutInflater> instances = new LayoutInflaterAppSingletonizer();

    private static LayoutInflater bind(LayoutInflater layoutInflater, Context context) {
//        shadowOf(layoutInflater).context = context;
        return layoutInflater;
    }

//    @Implementation
//    public static LayoutInflater from(Context context) {
//        return bind(instances.getInstance(context), context);
//    }

//    @Implementation
//    public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
//        String qualifiers = shadowOf(context.getResources().getConfiguration()).getQualifiers();
//        ResourceLoader resourceLoader = shadowOf(context.getResources()).getResourceLoader();
//        return new LayoutBuilder(resourceLoader).inflateView(context, resource, attachToRoot ? root : null, qualifiers);
//    }
//
//    @Implementation
//    public View inflate(int resource, ViewGroup root) {
//        return inflate(resource, root, root != null);
//    }

    private static class LayoutInflaterAppSingletonizer extends AppSingletonizer<LayoutInflater> {
        public LayoutInflaterAppSingletonizer() {
            super(LayoutInflater.class);
        }

        @Override protected LayoutInflater get(ShadowApplication shadowApplication) {
            return shadowApplication.getLayoutInflater();
        }

        @Override protected void set(ShadowApplication shadowApplication, LayoutInflater instance) {
            shadowApplication.layoutInflater = instance;
        }

        @Override protected LayoutInflater createInstance(Application applicationContext) {
            return new MyLayoutInflater(applicationContext);
        }

        private static class MyLayoutInflater extends LayoutInflater {
            public MyLayoutInflater(Context context) {
                super(context);
            }

            @Override public LayoutInflater cloneInContext(Context newContext) {
                return bind(new MyLayoutInflater(newContext), newContext);
            }
        }
    }
}
