package org.robolectric.shadows;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PasswordTransformationMethodTest {

    private ShadowPasswordTransformationMethod transformationMethod;

    @Before
    public void setUp(){
        transformationMethod = new ShadowPasswordTransformationMethod();
    }

    @Test
    public void shouldMaskInputCharacters(){
        CharSequence output = transformationMethod.getTransformation("foobar", null);
        assertThat(output.toString(), is("\u2022\u2022\u2022\u2022\u2022\u2022")); //using the escaped characters for cross platform compatibility.
    }

    @Test
    public void shouldTransformSpacesWithText(){
        CharSequence output = transformationMethod.getTransformation(" baz ", null);
        assertThat(output.toString(), is("\u2022\u2022\u2022\u2022\u2022"));
    }

    @Test
    public void shouldTransformSpacesWithoutText(){
        CharSequence output = transformationMethod.getTransformation("    ", null);
        assertThat(output.toString(), is("\u2022\u2022\u2022\u2022"));
    }

    @Test
    public void shouldNotTransformBlank(){
        CharSequence output = transformationMethod.getTransformation("", null);
        assertThat(output.toString(), is(""));
    }

    @Test
    public void shouldNotTransformNull(){
        CharSequence output = transformationMethod.getTransformation(null, null);
        assertThat(output.toString(), is(""));
    }

    @Test
    public void shouldRetrieveAnInstance(){
        assertThat(ShadowPasswordTransformationMethod.getInstance(), is(CoreMatchers.<Object>notNullValue()));
    }
}
