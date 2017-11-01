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

package jatf.common;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.annotation.Nonnull;

import org.reflections.Reflections;

import jatf.annotations.ArchitectureTest;

@SuppressWarnings("WeakerAccess")
public class ArchitectureTestAnnotationEvaluator extends ArchitectureTestAbstractEvaluator {

	public ArchitectureTestAnnotationEvaluator(@Nonnull Reflections reflections) {
		super(reflections);
	}

	@Override
	protected void process() {
		Set<Class<?>> annotatedClasses = newHashSet();
		Set<Class<?>> set = reflections.getTypesAnnotatedWith(ArchitectureTest.class);
		annotatedClasses.addAll(set);
		for (Class<?> annotatedClass : annotatedClasses) {
			ArchitectureTest annotation = annotatedClass.getAnnotation(ArchitectureTest.class);
			addAnnotatedClass(annotatedClass, annotation);
		}
	}
}
