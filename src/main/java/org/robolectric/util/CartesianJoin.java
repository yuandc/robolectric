package org.robolectric.util;

import java.util.ArrayList;
import java.util.List;

public class CartesianJoin {
  private List<Object[]> dimensions = new ArrayList<Object[]>();

  public void addDimension(Object... members) {
    dimensions.add(members);
  }

  public Object[][] generateSolutions() {
    int solutionCount = 1;
    for (Object[] dimension : dimensions) {
      solutionCount *= dimension.length;
    }

    Object[][] solutions = new Object[solutionCount][];
    for (int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++) {
      Object[] solution = new Object[dimensions.size()];
      solutions[solutionIndex] = solution;
      int j = 1;

      for (int dimIndex = 0; dimIndex < dimensions.size(); dimIndex++) {
        Object[] members = dimensions.get(dimIndex);
        solution[dimIndex] = members[((solutionIndex / j) % members.length)];
        j *= members.length;
      }
    }

    return solutions;
  }
}
