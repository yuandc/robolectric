package org.robolectric.shadows;

import android.util.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricConfig;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.TestConfigs;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class) @RobolectricConfig(TestConfigs.WithDefaults.class)
public class PairTest {

    @Test
    public void testConstructor() throws Exception {
        Pair<String, Integer> pair = new Pair<String, Integer>("a", 1);
        assertThat(pair.first, equalTo("a"));
        assertThat(pair.second, equalTo(1));
    }

    @Test
    public void testStaticCreate() throws Exception {
        Pair<String, String> p = Pair.create("Foo", "Bar");
        assertThat(p.first, equalTo("Foo"));
        assertThat(p.second, equalTo("Bar"));
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(Pair.create("1", 2), equalTo(Pair.create("1", 2)));
    }

    @Test
    public void testHash() throws Exception {
        assertThat(Pair.create("1", 2).hashCode(), equalTo(Pair.create("1", 2).hashCode()));
    }
}
