package org.robolectric;

import org.apache.maven.artifact.ant.DependenciesTask;
import org.apache.maven.model.Dependency;
import org.apache.tools.ant.Project;
import org.junit.runners.model.FrameworkMethod;
import org.robolectric.bytecode.AndroidTranslator;
import org.robolectric.bytecode.ClassCache;
import org.robolectric.bytecode.ClassHandler;
import org.robolectric.bytecode.RobolectricClassLoader;
import org.robolectric.bytecode.RobolectricInternals;
import org.robolectric.bytecode.Setup;
import org.robolectric.bytecode.ShadowWrangler;
import org.robolectric.res.AndroidResourcePathFinder;
import org.robolectric.res.PackageResourceLoader;
import org.robolectric.res.ResourceLoader;
import org.robolectric.res.ResourcePath;
import org.robolectric.res.RoutingResourceLoader;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class RobolectricContext {
    private static Map<AndroidManifest, ResourceLoader> resourceLoadersByAppManifest = new HashMap<AndroidManifest, ResourceLoader>();
    private static Map<ResourcePath, ResourceLoader> systemResourceLoaders = new HashMap<ResourcePath, ResourceLoader>();

    private final AndroidManifest appManifest;
    private final RobolectricClassLoader robolectricClassLoader;
    private final ClassHandler classHandler;

    public Class<? extends DefaultTestRun> getTestRunClass(FrameworkMethod method) {
        return DefaultTestRun.class;
    }

    public interface Factory {
        RobolectricContext createRobolectricContext();
    }

    public RobolectricContext() {
        ClassCache classCache = createClassCache();
        Setup setup = createSetup();
        classHandler = createClassHandler(setup);
        appManifest = createAppManifest();
        AndroidTranslator androidTranslator = createAndroidTranslator(setup, classCache);
        robolectricClassLoader = createRobolectricClassLoader(setup, classCache, androidTranslator);
    }

    public ResourceLoader getSystemResourceLoader(ResourcePath systemResourcePath) {
        ResourceLoader systemResourceLoader = systemResourceLoaders.get(systemResourcePath);
        if (systemResourceLoader == null) {
            systemResourceLoader = createResourceLoader(systemResourcePath);
            systemResourceLoaders.put(systemResourcePath, systemResourceLoader);
        }
        return systemResourceLoader;
    }

    public ResourceLoader getAppResourceLoader(ResourceLoader systemResourceLoader, final AndroidManifest appManifest) {
        ResourceLoader resourceLoader = resourceLoadersByAppManifest.get(appManifest);
        if (resourceLoader == null) {
            resourceLoader = createAppResourceLoader(systemResourceLoader, appManifest);
            resourceLoadersByAppManifest.put(appManifest, resourceLoader);
        }
        return resourceLoader;
    }

    // this method must live on a RobolectricClassLoader-loaded class, so it can't be on RobolectricContext
    protected ResourceLoader createAppResourceLoader(ResourceLoader systemResourceLoader, AndroidManifest appManifest) {
        Map<String, ResourceLoader> resourceLoaders = new HashMap<String, ResourceLoader>();

        List<ResourcePath> resourcePaths = new ArrayList<ResourcePath>();
        for (ResourcePath resourcePath : appManifest.getIncludedResourcePaths()) {
            resourcePaths.add(resourcePath);
        }
        PackageResourceLoader appResourceLoader = new PackageResourceLoader(resourcePaths, appManifest.getPackageName());
        for (ResourcePath resourcePath : appManifest.getIncludedResourcePaths()) {
            resourceLoaders.put(resourcePath.getPackageName(), appResourceLoader);
        }
        resourceLoaders.put("android", systemResourceLoader);
        return new RoutingResourceLoader(resourceLoaders);
    }

    protected PackageResourceLoader createResourceLoader(ResourcePath systemResourcePath) {
        return new PackageResourceLoader(systemResourcePath);
    }


    private ClassHandler createClassHandler(Setup setup) {
        System.out.println("ROBO: createClassHandler");
        return new ShadowWrangler(setup);
    }

    public ClassCache createClassCache() {
        System.out.println("ROBO: createClassCache");
        final String classCachePath = System.getProperty("cached.robolectric.classes.path");
        final File classCacheDirectory;
        if (null == classCachePath || "".equals(classCachePath.trim())) {
            classCacheDirectory = new File("./tmp");
        } else {
            classCacheDirectory = new File(classCachePath);
        }

        return new ClassCache(new File(classCacheDirectory, "cached-robolectric-classes.jar").getAbsolutePath(), AndroidTranslator.CACHE_VERSION);
    }

    public AndroidTranslator createAndroidTranslator(Setup setup, ClassCache classCache) {
        return new AndroidTranslator(classCache, setup);
    }

    protected AndroidManifest createAppManifest() {
        return new AndroidManifest(new File("."));
    }

    public AndroidManifest getAppManifest() {
        return appManifest;
    }

    public ClassHandler getClassHandler() {
        return classHandler;
    }

    public ResourcePath getSystemResourcePath() {
        AndroidManifest manifest = getAppManifest();
        return AndroidResourcePathFinder.getSystemResourcePath(manifest.getRealSdkVersion(), manifest.getResourcePath());
    }

    Class<?> bootstrapTestClass(Class<?> testClass) {
        Class<?> bootstrappedTestClass = robolectricClassLoader.bootstrap(testClass);
        return bootstrappedTestClass;
    }

    protected RobolectricClassLoader createRobolectricClassLoader(Setup setup, ClassCache classCache, AndroidTranslator androidTranslator) {
        final ClassLoader parentClassLoader = this.getClass().getClassLoader();
        ClassLoader realAndroidJarsClassLoader = new URLClassLoader(
                artifactUrls(realAndroidDependency("android-base"),
                        realAndroidDependency("android-kxml2"),
                        realAndroidDependency("android-luni"))
        , null) {
            @Override
            protected Class<?> findClass(String s) throws ClassNotFoundException {
                try {
                    return super.findClass(s);
                } catch (ClassNotFoundException e) {
                    return parentClassLoader.loadClass(s);
                }
            }
        };
        RobolectricClassLoader robolectricClassLoader = new RobolectricClassLoader(realAndroidJarsClassLoader, classCache, androidTranslator, setup);
        injectClassHandler(robolectricClassLoader);
        return robolectricClassLoader;
    }

    private void injectClassHandler(RobolectricClassLoader robolectricClassLoader) {
        try {
            Field field = robolectricClassLoader.loadClass(RobolectricInternals.class.getName()).getDeclaredField("classHandler");
            field.setAccessible(true);
            field.set(null, classHandler);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public RobolectricClassLoader getRobolectricClassLoader() {
        return robolectricClassLoader;
    }

    public Setup createSetup() {
        return new Setup();
    }

    private URL[] artifactUrls(Dependency... dependencies) {
        DependenciesTask dependenciesTask = new DependenciesTask();
        configureMaven(dependenciesTask);
        Project project = new Project();
        dependenciesTask.setProject(project);
        for (Dependency dependency : dependencies) {
            dependenciesTask.addDependency(dependency);
        }
        dependenciesTask.execute();

        @SuppressWarnings("unchecked")
        Hashtable<String, String> artifacts = project.getProperties();
        URL[] urls = new URL[artifacts.size()];
        int i = 0;
        for (String path : artifacts.values()) {
            try {
                urls[i++] = new URL("file://" + path);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

        return urls;
    }

    @SuppressWarnings("UnusedParameters")
    protected void configureMaven(DependenciesTask dependenciesTask) {
        // maybe you want to override this method and some settings?
    }

    private Dependency realAndroidDependency(String artifactId) {
        Dependency dependency = new Dependency();
        dependency.setGroupId("org.robolectric");
        dependency.setArtifactId(artifactId);
        dependency.setVersion("4.1.2_r1_rc");
        dependency.setType("jar");
        dependency.setClassifier("real");
        return dependency;
    }

    /** @deprecated use {@link org.robolectric.Robolectric.Reflection#setFinalStaticField(Class, String, Object)} */
    public static void setStaticValue(Class<?> clazz, String fieldName, Object value) {
        Robolectric.Reflection.setFinalStaticField(clazz, fieldName, value);
    }
}
