package jatf.web.controller;

public class JatfRoutes {

  public static final String PARAM_TEST_NAMES = "testNames";
  public static final String PARAM_CONSTRAINT = "constraint";
  private static final String PATH_BASE = "jatf/";
  public static final String PATH_RUN_TESTS = PATH_BASE + "runTests";
  public static final String PATH_GET_TEST_NAMES = PATH_BASE + "getTestNames";
  public static final String PATH_GET_CONSTRAINT = PATH_BASE + "getConstraint";

  private JatfRoutes() {
    // nop
  }
}
