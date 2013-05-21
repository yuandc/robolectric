package org.robolectric.internal;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.lang.reflect.Method;
import org.robolectric.AndroidManifest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestLifecycle;
import org.robolectric.res.ResourceLoader;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowActivityThread;
import org.robolectric.shadows.ShadowContextImpl;
import org.robolectric.shadows.ShadowResources;
import org.robolectric.util.DatabaseConfig;

import static org.fest.reflect.core.Reflection.constructor;
import static org.fest.reflect.core.Reflection.field;
import static org.fest.reflect.core.Reflection.method;
import static org.fest.reflect.core.Reflection.type;
import static org.robolectric.Robolectric.shadowOf;

public class ParallelUniverse implements ParallelUniverseInterface {
  private static final String DEFAULT_PACKAGE_NAME = "org.robolectric.default";
  private Class<?> contextImplClass;

  public void resetStaticState() {
    Robolectric.reset();
  }

  @Override public void setDatabaseMap(DatabaseConfig.DatabaseMap databaseMap) {
    DatabaseConfig.setDatabaseMap(databaseMap);
  }

  @Override public void setUpApplicationState(Method method, TestLifecycle testLifecycle, boolean strictI18n, ResourceLoader systemResourceLoader, AndroidManifest appManifest) {
    Robolectric.application = null;
    Robolectric.packageManager = new RobolectricPackageManager();
    Robolectric.packageManager.addPackage(DEFAULT_PACKAGE_NAME);
    if (appManifest != null) {
      Robolectric.packageManager.addPackage(appManifest);
    }

    ShadowResources.setSystemResources(systemResourceLoader);
    String qualifiers = RobolectricTestRunner.determineResourceQualifiers(method);
    Resources systemResources = Resources.getSystem();
    Configuration configuration = systemResources.getConfiguration();
    shadowOf(configuration).overrideQualifiers(qualifiers);
    systemResources.updateConfiguration(configuration, systemResources.getDisplayMetrics());

    contextImplClass = type(ShadowContextImpl.CLASS_NAME)
        .withClassLoader(getClass().getClassLoader())
        .load();

    Class<?> activityThreadClass = type(ShadowActivityThread.CLASS_NAME)
        .withClassLoader(getClass().getClassLoader())
        .load();

    Object activityThread = constructor()
        .in(activityThreadClass)
        .newInstance();

    ResourceLoader resourceLoader;
    if (appManifest != null) {
      resourceLoader = RobolectricTestRunner.getAppResourceLoader(systemResourceLoader, appManifest);
    } else {
      resourceLoader = systemResourceLoader;
    }

    Context systemContextImpl = (Context) method("createSystemContext")
        .withReturnType(contextImplClass)
        .withParameterTypes(activityThreadClass)
        .in(contextImplClass)
        .invoke(activityThread);

    final Application application = (Application) testLifecycle.createApplication(method, appManifest);
    if (application != null) {
      String packageName = appManifest != null ? appManifest.getPackageName() : null;
      if (packageName == null) packageName = DEFAULT_PACKAGE_NAME;

      ApplicationInfo applicationInfo;
      try {
        applicationInfo = Robolectric.packageManager.getApplicationInfo(packageName, 0);
      } catch (PackageManager.NameNotFoundException e) {
        throw new RuntimeException(e);
      }

      Class<?> compatibilityInfoClass = type("android.content.res.CompatibilityInfo").load();
      Object loadedApk = method("getPackageInfo")
          .withParameterTypes(ApplicationInfo.class, compatibilityInfoClass, ClassLoader.class, boolean.class, boolean.class)
          .in(activityThread)
          .invoke(applicationInfo, null, getClass().getClassLoader(), false, true);

      shadowOf(application).bind(appManifest, resourceLoader);
      if (appManifest == null) {
        // todo: make this cleaner...
        shadowOf(application).setPackageName(applicationInfo.packageName);
      }
      Resources appResources = application.getResources();
      field("mResources").ofType(Resources.class).in(loadedApk).set(appResources);

      Context contextImpl = method("createPackageContext")
          .withReturnType(Context.class)
          .withParameterTypes(String.class, int.class) // packageName, flags
          .in(systemContextImpl)
          .invoke(applicationInfo.packageName, Context.CONTEXT_INCLUDE_CODE);

      method("attach")
          .withParameterTypes(Context.class)
          .in(application)
          .invoke(contextImpl);

      appResources.updateConfiguration(configuration, appResources.getDisplayMetrics());
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
}
