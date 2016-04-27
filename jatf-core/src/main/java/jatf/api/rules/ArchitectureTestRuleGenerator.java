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

package jatf.api.rules;

import jatf.common.rules.AnnotationBasedRule;
import jatf.common.rules.ArchitectureTestAttributes;
import jatf.common.rules.ArchitectureTestRule;
import jatf.common.rules.RuleGenerator;
import jatf.common.rules.markers.ArchitectureTestMarker;
import jatf.common.rules.markers.RuleBasedMarker;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.util.ArchitectureTestUtil.createArchitectureTestAnnotation;

public abstract class ArchitectureTestRuleGenerator<M extends RuleBasedMarker, R extends AnnotationBasedRule<M, ? extends Condition<Class<?>>>> implements RuleGenerator<M, R> {

    private ArchitectureTestAttributes attributes;
    private Set<Class<?>> classes;

    public ArchitectureTestRuleGenerator() {
        classes = newHashSet();
        attributes = new ArchitectureTestAttributes();
    }

    @Nonnull
    @Override
    public ArchitectureTestRule generateArchitectureTestRuleFor(@Nonnull Class<?>[] classes) {
        ArchitectureTestMarker annotation = createArchitectureTestAnnotation(
                attributes.isOmitMetrics(),
                attributes.isOmitConventions(),
                attributes.getPatterns(),
                attributes.getDependencies());
        return new ArchitectureTestRule(classes, annotation);
    }

    @Nonnull
    public ArchitectureTestRule generateArchitectureTestRule() {
        return generateArchitectureTestRuleFor(classes.toArray(new Class<?>[classes.size()]));
    }

    @SuppressWarnings("unused")
    public ArchitectureTestAttributes getAttributes() {
        return attributes;
    }

    @SuppressWarnings("unused")
    public void setAttributes(ArchitectureTestAttributes attributes) {
        this.attributes = attributes;
    }

    public Class<?>[] getClasses() {
        return classes.toArray(new Class<?>[classes.size()]);
    }

    public void setClasses(Set<Class<?>> classes) {
        this.classes = classes;
    }
}
