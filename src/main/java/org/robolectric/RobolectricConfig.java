package org.robolectric;

/**
 * Indicates that the given subclass of RobolectricContext should be used to configure this test.
 */
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE})
public @interface RobolectricConfig {
    Class<? extends RobolectricContext> value();
}
