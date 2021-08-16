/**
 * This file is part of JATF.
 * <p>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.rules;

import jatf.annotations.Dependency;
import jatf.annotations.Pattern;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class ArchitectureTestAttributes {

  private boolean omitConventions;
  private boolean omitMetrics;
  private boolean enforceSecurityTests;

  private Set<Dependency> dependencies;
  private final Set<Pattern> patterns;
  private final Set<String> testNames;

  public ArchitectureTestAttributes() {
    this(false, false, true);
  }

  private ArchitectureTestAttributes(boolean omitMetrics, boolean omitConventions, boolean enforceSecurityTests) {
    this.omitMetrics = omitMetrics;
    this.omitConventions = omitConventions;
    this.enforceSecurityTests = enforceSecurityTests;
    dependencies = newHashSet();
    patterns = newHashSet();
    testNames = newHashSet();
  }

  /**
   * Creates test attributes with only the given dependency being active. No other tests would be run with the
   * resulting attributes.
   *
   * @param dependency - dependency to be used as a sole property in attributes
   * @return attributes with only the given dependency
   */
  @SuppressWarnings("unused")
  @Nonnull
  public static ArchitectureTestAttributes createWithDependencyOnly(@Nonnull Dependency dependency) {
    ArchitectureTestAttributes attributes = new ArchitectureTestAttributes(true, true, false);
    attributes.addDependency(dependency);
    return attributes;
  }

  /**
   * Creates test attributes with only the given test identified by testName being active. No other tests would be
   * run with the resulting attributes.
   *
   * @param testName - name of the test to be carried out exclusively
   * @return attributes with only the given test name
   */
  @SuppressWarnings("unused")
  @Nonnull
  public static ArchitectureTestAttributes createWithSingleTestOnly(@Nonnull String testName) {
    ArchitectureTestAttributes attributes = new ArchitectureTestAttributes(true, true, false);
    attributes.addTestName(testName);
    return attributes;
  }

  /**
   * Merges attributes with others in an optimistic fashion, that is, to minimize the number of tests being run by the
   * merge.
   *
   * @param other - other attributes to be merged into this instance
   * @return this instance (merged attributes)
   */
  @SuppressWarnings("unused")
  @Nonnull
  public ArchitectureTestAttributes mergeOptimistic(@Nonnull ArchitectureTestAttributes other) {
    if (other.omitConventions) {
      omitConventions = true;
    }
    if (other.omitMetrics) {
      omitMetrics = true;
    }
    if (!other.enforceSecurityTests) {
      enforceSecurityTests = false;
    }
    mergeExceptConventionsAndMetrics(other);
    return this;
  }

  /**
   * Merges attributes with others in a pessimistic fashion, that is, to maximize the number of tests being run by the
   * merge.
   *
   * @param other - other attributes to be merged into this instance
   * @return this instance (merged attributes)
   */
  @SuppressWarnings("unused")
  @Nonnull
  public ArchitectureTestAttributes mergePessimistic(@Nonnull ArchitectureTestAttributes other) {
    if (!other.omitConventions) {
      omitConventions = false;
    }
    if (!other.omitMetrics) {
      omitMetrics = false;
    }
    if (other.enforceSecurityTests) {
      enforceSecurityTests = true;
    }
    mergeExceptConventionsAndMetrics(other);
    return this;
  }

  public boolean isOmitConventions() {
    return omitConventions;
  }

  public boolean isOmitMetrics() {
    return omitMetrics;
  }

  @SuppressWarnings("unused")
  public void setOmitMetrics(boolean omitMetrics) {
    this.omitMetrics = omitMetrics;
  }

  public boolean isEnforceSecurityTests() {
    return enforceSecurityTests;
  }

  public Dependency[] getDependencies() {
    return dependencies.toArray(new Dependency[0]);
  }

  public void setDependencies(Set<Dependency> dependencies) {
    this.dependencies = dependencies;
  }

  @SuppressWarnings("WeakerAccess")
  public void addDependency(Dependency dependency) {
    dependencies.add(dependency);
  }

  public Pattern[] getPatterns() {
    return patterns.toArray(new Pattern[0]);
  }

  @SuppressWarnings("WeakerAccess")
  public void addTestName(String testName) {
    testNames.add(testName);
  }

  @SuppressWarnings("unused")
  public String[] getTestNames() {
    return testNames.toArray(new String[0]);
  }

  private void mergeExceptConventionsAndMetrics(@Nonnull ArchitectureTestAttributes other) {
    dependencies.addAll(other.dependencies);
    patterns.addAll(other.patterns);
    testNames.addAll(other.testNames);
  }
}
