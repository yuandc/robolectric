package org.robolectric.bytecode;

import java.lang.annotation.Annotation;

public interface ClassDesc {
    boolean isInterface();

    boolean isAnnotation();

    boolean hasAnnotation(Class<? extends Annotation> annotationClass);

    String getName();
}
