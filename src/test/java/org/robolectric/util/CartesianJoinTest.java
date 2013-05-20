package org.robolectric.util;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CartesianJoinTest {
  @Test public void shouldCreateSolutions1() throws Exception {
    CartesianJoin cartesianJoin = new CartesianJoin();
    cartesianJoin.addDimension("a");
    Object[][] solutions = cartesianJoin.generateSolutions();
    assertThat(solutions).isEqualTo(new Object[][] { new Object[] { "a" } });
  }

  @Test public void shouldCreateSolutions2x3() throws Exception {
    CartesianJoin cartesianJoin = new CartesianJoin();
    cartesianJoin.addDimension("a", "b");
    cartesianJoin.addDimension(1, 2, 3);
    Object[][] solutions = cartesianJoin.generateSolutions();
    assertThat(solutions).isEqualTo(new Object[][] {
        new Object[] { "a", 1 }, new Object[] { "b", 1 },
        new Object[] { "a", 2 }, new Object[] { "b", 2 },
        new Object[] { "a", 3 }, new Object[] { "b", 3 },
    });
  }

  @Test public void shouldCreateSolutions3x3() throws Exception {
    CartesianJoin cartesianJoin = new CartesianJoin();
    cartesianJoin.addDimension("a", "b", "c");
    cartesianJoin.addDimension(1, 2, 3);
    Object[][] solutions = cartesianJoin.generateSolutions();
    assertThat(solutions).isEqualTo(new Object[][] {
        new Object[] { "a", 1 }, new Object[] { "b", 1 }, new Object[] { "c", 1 },
        new Object[] { "a", 2 }, new Object[] { "b", 2 }, new Object[] { "c", 2 },
        new Object[] { "a", 3 }, new Object[] { "b", 3 }, new Object[] { "c", 3 },
    });
  }

  @Test public void shouldCreateSolutions2x2x2() throws Exception {
    CartesianJoin cartesianJoin = new CartesianJoin();
    cartesianJoin.addDimension("a", "b");
    cartesianJoin.addDimension(1, 2);
    cartesianJoin.addDimension("bunny", "mousey");
    Object[][] solutions = cartesianJoin.generateSolutions();
    assertThat(solutions).isEqualTo(new Object[][] {
        new Object[] { "a", 1, "bunny" }, new Object[] { "b", 1, "bunny" },
        new Object[] { "a", 2, "bunny" }, new Object[] { "b", 2, "bunny" },
        new Object[] { "a", 1, "mousey" }, new Object[] { "b", 1, "mousey" },
        new Object[] { "a", 2, "mousey" }, new Object[] { "b", 2, "mousey" },
    });
  }
}
