/**
 * This file is part of JATF.
 *
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
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
    private Set<Dependency> dependencies;
    private Set<Pattern> patterns;
    private Set<String> testNames;

    public ArchitectureTestAttributes() {
        this(false, false);
    }

    public ArchitectureTestAttributes(boolean omitMetrics, boolean omitConventions) {
        this.omitMetrics = omitMetrics;
        this.omitConventions = omitConventions;
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
    public static ArchitectureTestAttributes createWithDependencyOnly(@Nonnull Dependency dependency) {
        ArchitectureTestAttributes attributes = new ArchitectureTestAttributes(true, true);
        attributes.addDependency(dependency);
        return attributes;
    }

    public static ArchitectureTestAttributes createWithSingleTestOnly(@Nonnull String testName) {
        ArchitectureTestAttributes attributes = new ArchitectureTestAttributes(true, true);
        attributes.addTestName(testName);
        return attributes;
    }

    /**
     * Merges attributes with others in a maximizing fashion, that is, to maximize the number of tests being run by the
     * merge.
     *
     * @param other - other attributes to be merged into this instance
     * @return this instance (merged attributes)
     */
    public ArchitectureTestAttributes merge(ArchitectureTestAttributes other) {
        if (!other.omitConventions) {
            omitConventions = false;
        }
        if (!other.omitMetrics) {
            omitMetrics = false;
        }
        dependencies.addAll(other.dependencies);
        patterns.addAll(other.patterns);
        testNames.addAll(other.testNames);
        return this;
    }

    public boolean isOmitConventions() {
        return omitConventions;
    }

    public boolean isOmitMetrics() {
        return omitMetrics;
    }

    public void setOmitMetrics(boolean omitMetrics) {
        this.omitMetrics = omitMetrics;
    }

    public Dependency[] getDependencies() {
        return dependencies.toArray(new Dependency[dependencies.size()]);
    }

    public void setDependencies(Set<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(Dependency dependency) {
        dependencies.add(dependency);
    }

    public Pattern[] getPatterns() {
        return patterns.toArray(new Pattern[patterns.size()]);
    }

    public void addTestName(String testName) {
        testNames.add(testName);
    }

    public String[] getTestNames() {
        return testNames.toArray(new String[testNames.size()]);
    }
}
