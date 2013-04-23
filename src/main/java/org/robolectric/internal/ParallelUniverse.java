package org.robolectric.internal;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.BridgeInflater;
import com.android.ide.common.rendering.api.AdapterBinding;
import com.android.ide.common.rendering.api.ILayoutPullParser;
import com.android.ide.common.rendering.api.IProjectCallback;
import com.android.ide.common.rendering.api.LayoutLog;
import com.android.ide.common.rendering.api.RenderResources;
import com.android.ide.common.rendering.api.ResourceReference;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.ResourceResolver;
import com.android.ide.common.resources.configuration.FolderConfiguration;
import com.android.layoutlib.bridge.Bridge;
import com.android.layoutlib.bridge.android.BridgeContext;
import com.android.resources.ResourceType;
import com.android.util.Pair;
import org.robolectric.AndroidManifest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkEnvironment;
import org.robolectric.TestLifecycle;
import org.robolectric.res.ResName;
import org.robolectric.res.ResourceLoader;
import org.robolectric.res.RoutingResourceLoader;
import org.robolectric.shadows.ShadowResources;
import org.robolectric.util.DatabaseConfig;

import java.io.File;
import java.lang.reflect.Method;

import static org.robolectric.Robolectric.directlyOn;
import static org.robolectric.Robolectric.shadowOf;

public class ParallelUniverse implements ParallelUniverseInterface {
    public void resetStaticState() {
        Robolectric.reset();
    }

    @Override public void setDatabaseMap(DatabaseConfig.DatabaseMap databaseMap) {
        DatabaseConfig.setDatabaseMap(databaseMap);
    }

    @Override
    public void setUpApplicationState(Method method, TestLifecycle testLifecycle, boolean strictI18n, ResourceLoader systemResourceLoader, AndroidManifest appManifest) {
        Robolectric.application = null;

        new Bridge().init(null, new File("/Volumes/android/frameworks/base/data/fonts"), null, new LayoutLog());
//        BridgeAssetManager.initSystem();

        ShadowResources.setSystemResources(systemResourceLoader);
        String qualifiers = RobolectricTestRunner.determineResourceQualifiers(method);
        shadowOf(Resources.getSystem().getConfiguration()).overrideQualifiers(qualifiers);

        ResourceLoader resourceLoader = null;
        if (appManifest != null) {
            resourceLoader = RobolectricTestRunner.getAppResourceLoader(systemResourceLoader, appManifest);
        }

        final Application application = (Application) testLifecycle.createApplication(method, appManifest);
        if (application != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.densityDpi = 240;
            metrics.density = 240f / 160;
            metrics.scaledDensity = metrics.density;
            metrics.widthPixels = 800;
            metrics.heightPixels = 1080;
            metrics.xdpi = 240;
            metrics.ydpi = 240;

            ResourceRepository systemResourceRepository = ((SdkEnvironment.SystemPackageResourceLoader) systemResourceLoader).getResourceRepository();
            ResourceRepository appResourceRepository = ((RoutingResourceLoader) resourceLoader).getFrameworkResources();

            FolderConfiguration referenceConfig = FolderConfiguration.getConfig(new String[] {
                    "boogers", "en", "rUS", "w600dp", "h1024dp", "large", "port", "mdpi", "finger", "nokeys", "v12"});
            RenderResources renderResources = ResourceResolver.create(
                    appResourceRepository.getConfiguredResources(referenceConfig),
                    systemResourceRepository.getConfiguredResources(referenceConfig),
                    null,
                    false
            );
            Configuration config = new Configuration();
            MyProjectCallback projectCallback = new MyProjectCallback(appManifest, appResourceRepository, resourceLoader);
            BridgeContext bridgeContext = new BridgeContext(new Object(), metrics, renderResources,
                    projectCallback, config, appManifest.getTargetSdkVersion());
            bridgeContext.initResources();
            bridgeContext.setBridgeInflater(new BridgeInflater(bridgeContext, projectCallback));

            directlyOn(application, ContextWrapper.class, "attachBaseContext", Context.class).invoke(bridgeContext);

            shadowOf(application).bind(appManifest, resourceLoader, systemResourceRepository, appResourceRepository);
            shadowOf(application.getResources().getConfiguration()).overrideQualifiers(qualifiers);
            shadowOf(application).setStrictI18n(strictI18n);

            Robolectric.application = application;
            application.onCreate();
        }
    }

    @Override public void tearDownApplication() {
        if (Robolectric.application != null) {
            Robolectric.application.onTerminate();
        }
    }

    @Override public Object getCurrentApplication() {
        return Robolectric.application;
    }

    private static class MyProjectCallback implements IProjectCallback {
        private final AndroidManifest appManifest;
        private final ResourceRepository resourceRepository;
        private final ResourceLoader resourceLoader;

        public MyProjectCallback(AndroidManifest appManifest, ResourceRepository resourceRepository, ResourceLoader resourceLoader) {
            this.appManifest = appManifest;
            this.resourceRepository = resourceRepository;
            this.resourceLoader = resourceLoader;
        }

        @Override
        public Object loadView(String name, Class[] constructorSignature, Object[] constructorArgs) throws ClassNotFoundException, Exception {
            throw new UnsupportedOperationException();
        }

        @Override public String getNamespace() {
            return appManifest.getPackageName();
        }

        @Override public Pair<ResourceType, String> resolveResourceId(int id) {
            ResName resName = resourceLoader.getResourceIndex().getResName(id);
            return Pair.of(ResourceType.getEnum(resName.type), resName.name);
        }

        @Override public String resolveResourceId(int[] id) {
            throw new UnsupportedOperationException();
        }

        @Override public Integer getResourceId(ResourceType type, String name) {
            ResName resName = new ResName(getNamespace(), type.getName(), name);
            return resourceLoader.getResourceIndex().getResourceId(resName);
        }

        @Override public ILayoutPullParser getParser(String layoutName) {
            throw new UnsupportedOperationException();
        }

        @Override public ILayoutPullParser getParser(ResourceValue layoutResource) {
            return null;
        }

        @Override
        public Object getAdapterItemValue(ResourceReference adapterView, Object adapterCookie, ResourceReference itemRef, int fullPosition, int positionPerType, int fullParentPosition, int parentPositionPerType, ResourceReference viewRef, ViewAttribute viewAttribute, Object defaultValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public AdapterBinding getAdapterBinding(ResourceReference adapterViewRef, Object adapterCookie, Object viewObject) {
            throw new UnsupportedOperationException();
        }
    }
}
