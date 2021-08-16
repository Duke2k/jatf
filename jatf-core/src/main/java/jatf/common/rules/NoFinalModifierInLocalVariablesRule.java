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

import jatf.annotations.ArchitectureTest;
import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.rules.conditions.HasAnnotation;
import jatf.common.rules.conditions.Not;
import jatf.common.rules.markers.NullMarker;

/**
 * This rule determines if the given classes have final modifiers in local variables. Generally, it is unnecessary to
 * declare local variables final, since javac does this at compile-time, where applicable. Using 'final' in this
 * context is therefore commonly considered as clogging up code unnecessarily.
 * The rule does this only if no other architecture tests are defined by annotations for any of the given classes.
 */
@SuppressWarnings({"unused", "rawtypes"})
public final class NoFinalModifierInLocalVariablesRule extends ClassAnnotationBasedRule<NullMarker, Not> {

  public NoFinalModifierInLocalVariablesRule(Class<?>[] classes) {
    super(classes, new Not(new HasAnnotation(ArchitectureTest.class)), new NullMarker());
  }
}
