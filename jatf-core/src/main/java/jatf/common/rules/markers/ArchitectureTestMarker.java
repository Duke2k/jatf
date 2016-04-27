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

package jatf.common.rules.markers;

import jatf.annotations.ArchitectureTest;
import jatf.annotations.Dependency;
import jatf.annotations.Pattern;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class ArchitectureTestMarker extends RuleBasedMarker<ArchitectureTest> {

    public boolean omitMetrics = false;
    public boolean omitConventions = false;
    public Pattern[] patterns = new Pattern[0];
    public Dependency[] dependencies = new Dependency[0];

    private String[] testNames = new String[0];

    @Override
    public Class<ArchitectureTest> annotationType() {
        return ArchitectureTest.class;
    }

    @Override
    public ArchitectureTest createAnnotation() {
        return new ArchitectureTest() {

            @Override
            public String toString() {
                return annotationType().getSimpleName();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ArchitectureTest.class;
            }

            @Override
            public boolean omitMetrics() {
                return omitMetrics;
            }

            @Override
            public boolean omitConventions() {
                return omitConventions;
            }

            @Override
            public Pattern[] patterns() {
                return patterns;
            }

            @Override
            public Dependency[] dependencies() {
                return dependencies;
            }

            @Override
            public String[] testNames() {
                return testNames;
            }
        };
    }

    public ArchitectureTestMarker merge(ArchitectureTestMarker other) {
        if (other.omitMetrics) {
            this.omitMetrics = true;
        }
        if (other.omitConventions) {
            this.omitConventions = true;
        }
        Set<Pattern> mergedPatternSet = newHashSet();
        Set<Dependency> mergedDependencySet = newHashSet();
        Set<String> mergedTestNames = newHashSet();
        mergedPatternSet.addAll(Arrays.asList(other.patterns));
        mergedPatternSet.addAll(Arrays.asList(this.patterns));
        mergedDependencySet.addAll(Arrays.asList(other.dependencies));
        mergedDependencySet.addAll(Arrays.asList(this.dependencies));
        mergedTestNames.addAll(Arrays.asList(other.testNames));
        mergedTestNames.addAll(Arrays.asList(this.testNames));
        patterns = mergedPatternSet.toArray(new Pattern[mergedPatternSet.size()]);
        dependencies = mergedDependencySet.toArray(new Dependency[mergedDependencySet.size()]);
        testNames = mergedTestNames.toArray(new String[mergedTestNames.size()]);
        return this;
    }
}
