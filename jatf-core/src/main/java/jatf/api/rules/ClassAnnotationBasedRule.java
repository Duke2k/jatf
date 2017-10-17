/*
  This file is part of JATF.

  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.

  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.api.rules;

import jatf.common.rules.AnnotationBasedRule;
import jatf.common.rules.markers.RuleBasedMarker;

import javax.annotation.Nonnull;

public abstract class ClassAnnotationBasedRule<M extends RuleBasedMarker, C extends Condition<Class<?>>> extends AnnotationBasedRule<M, C> {

    protected ClassAnnotationBasedRule(Class<?>[] classes, C condition, M marker) {
        super(condition, marker);
        for (Class<?> clazz : classes) {
            addClassIfConditionFires(clazz);
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void addClassIfConditionFires(@Nonnull Class<?> clazz) {
        if (condition.firesFor(clazz)) {
            classes.add(clazz);
        }
    }
}
