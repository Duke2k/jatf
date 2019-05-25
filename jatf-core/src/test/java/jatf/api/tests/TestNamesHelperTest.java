package jatf.api.tests;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TestNamesHelperTest {

  @Test
  public void getTestNames() {
    assertEquals(28, TestNamesHelper.getTestNames().size());
    assertThat(TestNamesHelper.getTestNames(), contains("CamelCaseTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("NoFinalModifierInLocalVariablesTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("NoInnerClassesTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("OrderingTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("OverlyChainedMethodCallsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("AcyclicDependenciesPrincipleTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("AnnotationTypeTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("DangerousCastsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("ExtendsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("ImplementsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("InstabilityTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("MethodPurityTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("OverridesTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("ReturnsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("ThrowsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("UncheckedCastsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("UsesTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("CyclomaticComplexityTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("HalsteadComplecityTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("MethodLengthTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("NumberOfMethodsPerClassTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("AdapterTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("SingletonTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("StateTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("StrategyTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("TemplateMethodTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("SecureSqlStatementsTest"));
    assertThat(TestNamesHelper.getTestNames(), contains("FileUserRightsTest"));
  }

  private TypeSafeMatcher<Set<String>> contains(String s) {
    return new TypeSafeMatcher<Set<String>>() {
      @Override
      protected boolean matchesSafely(Set<String> strings) {
        return strings.contains(s);
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("String '" + s + "' is not present.");
      }
    };
  }
}