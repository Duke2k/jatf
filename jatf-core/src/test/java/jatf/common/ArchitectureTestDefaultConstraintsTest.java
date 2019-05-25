package jatf.common;

import jatf.api.constraints.Constraint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ArchitectureTestDefaultConstraintsTest {

  @Test
  public void valueOf_Valid() {
    // prepare and test
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    ArchitectureTestDefaultConstraints defaultConstraints = new ArchitectureTestDefaultConstraints();

    // verify
    assertEquals(Constraint.values().length, defaultConstraints.entrySet().size());
    assertEquals("0.8", defaultConstraints.valueOf(Constraint.INSTABILITY_LOOSE));
    assertEquals("0.4", defaultConstraints.valueOf(Constraint.INSTABILITY_STRICT));
    assertEquals("5", defaultConstraints.valueOf(Constraint.MAX_CHAINED_METHOD_CALLS));
    assertEquals("10", defaultConstraints.valueOf(Constraint.MAX_DEPTH_FOR_DFS));
    assertEquals("5", defaultConstraints.valueOf(Constraint.MAX_HALSTEAD_DELIVERED_BUGS));
    assertEquals("20", defaultConstraints.valueOf(Constraint.MAX_NUMBER_OF_METHODS_PER_CLASS));
    assertEquals("20", defaultConstraints.valueOf(Constraint.MAX_NUMBER_OF_STATEMENTS_PER_METHOD));
    assertEquals("10", defaultConstraints.valueOf(Constraint.MAXIMUM_CCN));
    assertEquals("0.5", defaultConstraints.valueOf(Constraint.MIN_DEGREE_OF_PURITY));
    assertEquals(".", defaultConstraints.valueOf(Constraint.ROOT_FOLDER));
    assertEquals("jatf", defaultConstraints.valueOf(Constraint.SCOPES));
    assertEquals("false", defaultConstraints.valueOf(Constraint.WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER));
  }
}