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

package jatf.api.rules;

import static com.google.common.collect.Sets.newHashSet;

import java.lang.reflect.Method;
import java.util.Set;

import javax.annotation.Nonnull;

import jatf.common.rules.AnnotationBasedRule;
import jatf.common.rules.markers.RuleBasedMarker;

public abstract class MethodAnnotationBasedRule<M extends RuleBasedMarker, C extends Condition<Class<?>>> extends AnnotationBasedRule<M, C> {

	protected Set<Method> methods;

	protected MethodAnnotationBasedRule(Method[] methods, C condition, M marker) {
		super(condition, marker);
		this.methods = newHashSet();
		for (Method method : methods) {
			addMethodIfConditionFires(method);
		}
	}

	@SuppressWarnings("WeakerAccess")
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
