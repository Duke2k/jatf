package jatf.api.tests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestsHelperTest {

  @Test
  public void getDefaultConstraintsType() {
    assertEquals("ArchitectureTestDefaultConstraints", TestsHelper.getDefaultConstraintsType().getSimpleName());
  }

  @Test
  public void getDefaultConstraints() {
    assertEquals("ArchitectureTestDefaultConstraints", TestsHelper.getDefaultConstraints().getClass().getSimpleName());
  }
}