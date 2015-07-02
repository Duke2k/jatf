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

import jatf.common.rules.conditions.Condition;
import jatf.common.rules.markers.RuleBasedMarker;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public abstract class MethodAnnotationBasedRule<M extends RuleBasedMarker, C extends Condition<Class<?>>> extends AnnotationBasedRule<M, C> {

    protected Set<Method> methods;

    protected MethodAnnotationBasedRule(Method[] methods, C condition, M marker) {
        super(condition, marker);
        this.methods = newHashSet();
        for (Method method : methods) {
            addMethodIfConditionFires(method);
        }
    }

    public void addMethodIfConditionFires(@Nonnull Method method) {
        if (condition.firesFor(method.getDeclaringClass())) {
            methods.add(method);
            classes.add(method.getDeclaringClass());
        }
    }

    public Method[] getMethods() {
        return methods.toArray(new Method[methods.size()]);
    }
}
