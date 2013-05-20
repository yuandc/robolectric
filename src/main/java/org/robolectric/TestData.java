package org.robolectric;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class TestData<T> implements MethodRule {
    private final T[] values;
    private T currentValue;

    public TestData(T... values) {
        this.values = values;
    }

    @Override public Statement apply(final Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override public void evaluate() throws Throwable {
                for (T value : values) {
                    currentValue = value;
                    base.evaluate();
                }
            }
        };
    }

    public T value() {
        return currentValue;
    }

    T[] allValues() {
        return values;
    }
}
