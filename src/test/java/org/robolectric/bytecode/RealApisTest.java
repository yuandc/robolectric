package org.robolectric.bytecode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;
import org.robolectric.internal.Implements;
import org.robolectric.internal.Instrument;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.RealApisWithoutDefaults.class)
public class RealApisTest {
    @Test
    public void whenShadowHandlerIsInRealityBasedMode_shouldNotCallRealForUnshadowedMethod() throws Exception {
        Robolectric.getShadowWrangler().bindShadowClass(Pony.ShadowPony.class);

        assertEquals("Off I saunter to the salon!", new Pony("abc").saunter("the salon"));
    }

    @Test
    public void shouldCallOriginalConstructorBodySomehow() throws Exception {
        Robolectric.getShadowWrangler().bindShadowClass(ShadowOfClassWithSomeConstructors.class);
        ClassWithSomeConstructors o = new ClassWithSomeConstructors("my name");
        assertEquals("my name", o.name);
    }

    @Instrument
    public static class ClassWithSomeConstructors {
        public String name;

        public ClassWithSomeConstructors(String name) {
            this.name = name;
        }
    }

    @Implements(ClassWithSomeConstructors.class)
    public static class ShadowOfClassWithSomeConstructors {
    }
}
