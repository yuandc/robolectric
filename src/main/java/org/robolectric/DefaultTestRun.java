package org.robolectric;

import android.app.Application;
import android.content.res.Resources;
import org.junit.runners.model.FrameworkMethod;
import org.robolectric.annotation.DisableStrictI18n;
import org.robolectric.annotation.EnableStrictI18n;
import org.robolectric.annotation.Values;
import org.robolectric.bytecode.ClassHandler;
import org.robolectric.bytecode.ShadowWrangler;
import org.robolectric.res.ResourceLoader;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowResources;
import org.robolectric.util.DatabaseConfig;
import org.robolectric.util.SQLiteMap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.robolectric.Robolectric.shadowOf;

public class DefaultTestRun implements TestRun {
    private final FrameworkMethod frameworkMethod;
    private final DatabaseConfig.DatabaseMap databaseMap;
    private RobolectricContext robolectricContext;

    public DefaultTestRun(RobolectricContext robolectricContext, FrameworkMethod frameworkMethod) {
        this.robolectricContext = robolectricContext;
        this.frameworkMethod = frameworkMethod;
        databaseMap = setupDatabaseMap(frameworkMethod.getMethod().getDeclaringClass(), new SQLiteMap());
    }

    /*
     * Specifies what database to use for testing (ex: H2 or Sqlite),
     * this will load H2 by default, the SQLite TestRunner version will override this.
     */
    protected DatabaseConfig.DatabaseMap setupDatabaseMap(Class<?> testClass, DatabaseConfig.DatabaseMap map) {
        DatabaseConfig.DatabaseMap dbMap = map;

        if (testClass.isAnnotationPresent(DatabaseConfig.UsingDatabaseMap.class)) {
            DatabaseConfig.UsingDatabaseMap usingMap = testClass.getAnnotation(DatabaseConfig.UsingDatabaseMap.class);
            if (usingMap.value() != null) {
                dbMap = Robolectric.newInstanceOf(usingMap.value());
            } else {
                if (dbMap == null)
                    throw new RuntimeException("UsingDatabaseMap annotation value must provide a class implementing DatabaseMap");
            }
        }
        return dbMap;
    }

    /*
     * Called before each test method is run. Sets up the simulation of the Android runtime environment.
     */
    @Override
    final public void internalBeforeTest() {
        setupLogging();
        configureShadows(frameworkMethod.getMethod());

        Robolectric.resetStaticState();
        resetStaticState();

        DatabaseConfig.setDatabaseMap(databaseMap); //Set static DatabaseMap in DBConfig

        setupApplicationState(frameworkMethod.getMethod());

        beforeTest(frameworkMethod.getMethod());
    }

    /**
     * Override this method to reset the state of static members before each test.
     */
    protected void resetStaticState() {
    }

    @Override
    public void internalAfterTest() {
        afterTest(frameworkMethod.getMethod());
    }

    /**
     * Called before each test method is run.
     *
     * @param method the test method about to be run
     */
    @Override
    public void beforeTest(final Method method) {
    }

    /**
     * Called after each test method is run.
     *
     * @param method the test method that just ran.
     */
    @Override
    public void afterTest(final Method method) {
    }

    @Override
    public void prepareTest(final Object test) {
    }

    @Override
    public void setupApplicationState(Method testMethod) {
        boolean strictI18n = determineI18nStrictState(testMethod);

        ResourceLoader systemResourceLoader = robolectricContext.getSystemResourceLoader(robolectricContext.getSystemResourcePath());
        ShadowResources.setSystemResources(systemResourceLoader);

        ClassHandler classHandler = robolectricContext.getClassHandler();
        classHandler.setStrictI18n(strictI18n);

        AndroidManifest appManifest = robolectricContext.getAppManifest();
        ResourceLoader resourceLoader = robolectricContext.getAppResourceLoader(systemResourceLoader, appManifest);

        Robolectric.application = ShadowApplication.bind(createApplication(), appManifest, resourceLoader);
        shadowOf(Robolectric.application).setStrictI18n(strictI18n);

        String qualifiers = determineResourceQualifiers(testMethod);
        shadowOf(Resources.getSystem().getConfiguration()).overrideQualifiers(qualifiers);
        shadowOf(Robolectric.application.getResources().getConfiguration()).overrideQualifiers(qualifiers);
    }

    protected void configureShadows(Method testMethod) { // todo: dedupe this/bindShadowClasses
        ShadowWrangler shadowWrangler = (ShadowWrangler) robolectricContext.getClassHandler();
        for (Class<?> shadowClass : Robolectric.getDefaultShadowClasses()) {
            shadowWrangler.bindShadowClass(shadowClass);
        }
        bindShadowClasses(testMethod);
    }

    /**
     * Override this method if you want to provide your own implementation of Application.
     * <p/>
     * This method attempts to instantiate an application instance as specified by the AndroidManifest.xml.
     *
     * @return An instance of the Application class specified by the ApplicationManifest.xml or an instance of
     *         Application if not specified.
     */
    protected Application createApplication() {
        return new ApplicationResolver(robolectricContext.getAppManifest()).resolveApplication();
    }

    private void setupLogging() {
        String logging = System.getProperty("robolectric.logging");
        if (logging != null && ShadowLog.stream == null) {
            PrintStream stream = null;
            if ("stdout".equalsIgnoreCase(logging)) {
                stream = System.out;
            } else if ("stderr".equalsIgnoreCase(logging)) {
                stream = System.err;
            } else {
                try {
                    final PrintStream file = new PrintStream(new FileOutputStream(logging));
                    stream = file;
                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        @Override public void run() {
                            try { file.close(); } catch (Exception ignored) { }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            ShadowLog.stream = stream;
        }
    }

    private String determineResourceQualifiers(Method method) {
        String qualifiers = "";
        Values values = method.getAnnotation(Values.class);
        if (values != null) {
            qualifiers = values.qualifiers();
            if (qualifiers.isEmpty()) {
                qualifiers = values.locale();
            }
        }
        return qualifiers;
    }

    /**
     * Override this method to bind your own shadow classes
     */
    @SuppressWarnings("UnusedParameters")
    protected void bindShadowClasses(Method testMethod) {
        bindShadowClasses();
    }

    /**
     * Override this method to bind your own shadow classes
     */
    protected void bindShadowClasses() {
    }

    /**
     * Sets Robolectric config to determine if Robolectric should blacklist API calls that are not
     * I18N/L10N-safe.
     * <p/>
     * I18n-strict mode affects suitably annotated shadow methods. Robolectric will throw exceptions
     * if these methods are invoked by application code. Additionally, Robolectric's ResourceLoader
     * will throw exceptions if layout resources use bare string literals instead of string resource IDs.
     * <p/>
     * To enable or disable i18n-strict mode for specific test cases, annotate them with
     * {@link org.robolectric.annotation.EnableStrictI18n} or
     * {@link org.robolectric.annotation.DisableStrictI18n}.
     * <p/>
     *
     * By default, I18n-strict mode is disabled.
     *
     * @param method
     *
     */
    private boolean determineI18nStrictState(Method method) {
        // Global
        boolean strictI18n = globalI18nStrictEnabled();

        // Test case class
        Class<?> testClass = method.getDeclaringClass();
        if (testClass.getAnnotation(EnableStrictI18n.class) != null) {
            strictI18n = true;
        } else if (testClass.getAnnotation(DisableStrictI18n.class) != null) {
            strictI18n = false;
        }

        // Test case method
        if (method.getAnnotation(EnableStrictI18n.class) != null) {
            strictI18n = true;
        } else if (method.getAnnotation(DisableStrictI18n.class) != null) {
            strictI18n = false;
        }

        return strictI18n;
    }

    /**
     * Default implementation of global switch for i18n-strict mode.
     * To enable i18n-strict mode globally, set the system property
     * "robolectric.strictI18n" to true. This can be done via java
     * system properties in either Ant or Maven.
     * <p/>
     * Subclasses can override this method and establish their own policy
     * for enabling i18n-strict mode.
     *
     * @return
     */
    protected boolean globalI18nStrictEnabled() {
        return Boolean.valueOf(System.getProperty("robolectric.strictI18n"));
    }
}
