package org.robolectric;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.robolectric.bytecode.RobolectricClassLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Installs a {@link RobolectricClassLoader} and {@link org.robolectric.res.ResourceLoader} in order to
 * provide a simulation of the Android runtime environment.
 */
public class RobolectricTestRunner extends BlockJUnit4ClassRunner {
    private static Map<Class<? extends RobolectricContext>, RobolectricContext> contexts = new HashMap<Class<? extends RobolectricContext>, RobolectricContext>();
    private final RobolectricContext robolectricContext;

    private Class<?> bootstrappedTestClass;

    // fields in the RobolectricTestRunner in the original ClassLoader
    private RobolectricTestRunner.MyBlockJUnit4ClassRunner helperRunner;

    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public RobolectricTestRunner(final Class<?> testClass) throws InitializationError {
        super(testClass);

        robolectricContext = getRobolectricContext(testClass);
    }

    private RobolectricContext getRobolectricContext(Class<?> testClass) {
        RobolectricConfig robolectricConfig = testClass.getAnnotation(RobolectricConfig.class);
        Class<? extends RobolectricContext> contextClass = robolectricConfig == null ? null : robolectricConfig.value();
        if (contextClass == null) {
            contextClass = RobolectricContext.class;
        }
        RobolectricContext robolectricContext = contexts.get(contextClass);
        if (robolectricContext == null) {
            try {
                contexts.put(contextClass, robolectricContext = contextClass.newInstance());
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return robolectricContext;
    }

    protected static boolean isBootstrapped(Class<?> clazz) {
        return clazz.getClassLoader() instanceof RobolectricClassLoader;
    }

    @Override public Statement methodBlock(final FrameworkMethod method) {
        ensureBootstrappedTestClass();

        robolectricContext.getClassHandler().reset();

        final FrameworkMethod bootstrappedMethod;
        try {
            bootstrappedMethod = new FrameworkMethod(bootstrappedTestClass.getMethod(method.getName(), method.getMethod().getParameterTypes()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        RobolectricClassLoader robolectricClassLoader = robolectricContext.getRobolectricClassLoader();
        final TestRun testRun;
        try {
            Class<? extends TestRun> testRunClass = robolectricClassLoader.loadClass(robolectricContext.getTestRunClass(bootstrappedMethod).getName());
            testRun = testRunClass.getConstructor(RobolectricContext.class, FrameworkMethod.class).newInstance(robolectricContext, method);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            testRun.internalBeforeTest();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        final Statement statement = helperRunner.methodBlock(bootstrappedMethod);
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                HashMap<Field,Object> withConstantAnnos = getWithConstantAnnotations(method.getMethod());

                // todo: this try/finally probably isn't right -- should mimic RunAfters? [xw]
                try {
                    if (withConstantAnnos.isEmpty()) {
                        statement.evaluate();
                    }
                    else {
                        synchronized(this) {
                            setupConstants(withConstantAnnos);
                            statement.evaluate();
                            setupConstants(withConstantAnnos);
                        }
                    }
                } finally {
                    testRun.internalAfterTest();
                }
            }
        };
    }

    /**
     * You probably don't want to override this method. Override #prepareTest(Object) instead.
     *
     * @see org.junit.runners.BlockJUnit4ClassRunner#createTest()
     */
    @Override
    public Object createTest() throws Exception {
        ensureBootstrappedTestClass();

        Object test = new TestClass(bootstrappedTestClass).getOnlyConstructor().newInstance();
//        prepareTest(test);
        return test;
    }

    private void ensureBootstrappedTestClass() {
        if (bootstrappedTestClass == null) {
            bootstrappedTestClass = robolectricContext.bootstrapTestClass(getTestClass().getJavaClass());
            try {
                helperRunner = new MyBlockJUnit4ClassRunner();
            } catch (InitializationError initializationError) {
                throw new RuntimeException(initializationError);
            }

            Thread.currentThread().setContextClassLoader(robolectricContext.getRobolectricClassLoader());
        }
    }

    /**
	 * Find all the class and method annotations and pass them to
	 * addConstantFromAnnotation() for evaluation.
	 *
	 * TODO: Add compound annotations to suport defining more than one int and string at a time
	 * TODO: See http://stackoverflow.com/questions/1554112/multiple-annotations-of-the-same-type-on-one-element
	 *
	 * @param method
	 * @return
	 */
    private HashMap<Field,Object> getWithConstantAnnotations(Method method) {
    	HashMap<Field,Object> constants = new HashMap<Field,Object>();

    	for(Annotation anno:method.getDeclaringClass().getAnnotations()) {
    		addConstantFromAnnotation(constants, anno);
    	}

    	for(Annotation anno:method.getAnnotations()) {
    		addConstantFromAnnotation(constants, anno);
    	}

    	return constants;
    }


    /**
     * If the annotation is a constant redefinition, add it to the provided hash
     *
     * @param constants
     * @param anno
     */
    private void addConstantFromAnnotation(HashMap<Field,Object> constants, Annotation anno) {
        try {
        	String name = anno.annotationType().getName();
        	Object newValue = null;
    	
	    	if (name.equals( "org.robolectric.annotation.WithConstantString" )) {
	    		newValue = (String) anno.annotationType().getMethod("newValue").invoke(anno);
	    	} 
	    	else if (name.equals( "org.robolectric.annotation.WithConstantInt" )) {
	    		newValue = (Integer) anno.annotationType().getMethod("newValue").invoke(anno);
	    	}
	    	else {
	    		return;
	    	}

    		@SuppressWarnings("rawtypes")
			Class classWithField = (Class) anno.annotationType().getMethod("classWithField").invoke(anno);
    		String fieldName = (String) anno.annotationType().getMethod("fieldName").invoke(anno);
            Field field = classWithField.getDeclaredField(fieldName);
            constants.put(field, newValue);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Defines static finals from the provided hash and stores the old values back
     * into the hash.
     *
     * Call it twice with the same hash, and it puts everything back the way it was originally.
     *
     * @param constants
     */
    private void setupConstants(HashMap<Field,Object> constants) {
    	for(Field field:constants.keySet()) {
    		Object newValue = constants.get(field);
    		Object oldValue = Robolectric.Reflection.setFinalStaticField(field, newValue);
    		constants.put(field,oldValue);
    	}
    }

    private class MyBlockJUnit4ClassRunner extends BlockJUnit4ClassRunner {
        public MyBlockJUnit4ClassRunner() throws InitializationError {
            super(RobolectricTestRunner.this.bootstrappedTestClass);
        }

        @Override
        protected Statement methodBlock(FrameworkMethod method) {
            return super.methodBlock(method);
        }
    }
}
