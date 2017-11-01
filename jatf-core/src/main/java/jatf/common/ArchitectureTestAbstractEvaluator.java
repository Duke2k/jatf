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

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.reflections.Reflections;

import jatf.annotations.ArchitectureTest;
import jatf.annotations.Dependency;
import jatf.annotations.Exclude;
import jatf.annotations.Pattern;

@SuppressWarnings("WeakerAccess")
public abstract class ArchitectureTestAbstractEvaluator {

	protected Map<String, Set<Class<?>>> sourceMap;
	protected Reflections reflections;

	public ArchitectureTestAbstractEvaluator(@Nonnull Reflections reflections) {
		this.reflections = reflections;
		sourceMap = newHashMap();
	}

	/**
	 * This method merges the source map of classes into the target map. Basically, both structures are the same,
	 * but the source map is a separate instance for each concrete evaluator.
	 *
	 * @param targetMap - the map into which the classes to be tested should be put, keyed by the test names
	 */
	public void evaluateInto(@Nonnull Map<String, Set<Class<?>>> targetMap) {
		process();
		for (String sourceKey : sourceMap.keySet()) {
			Set<Class<?>> targetValues = targetMap.get(sourceKey);
			if (targetValues == null) {
				targetValues = newHashSet();
			}
			targetValues.addAll(sourceMap.get(sourceKey));
			targetMap.put(sourceKey, targetValues);
		}
	}

	/**
	 * Abstract method, to be implemented depending on the concrete case of how classes for architecture tests are to
	 * be determined. Currently, there are two implementations - one for annotations, and one for rules.
	 */
	protected abstract void process();

	protected void addAnnotatedClass(@Nonnull Class<?> clazz, @Nonnull ArchitectureTest annotation) {
		processAnnotationsForMetrics(clazz, annotation);
		processAnnotationsForConventions(clazz, annotation);
		processAnnotationsForDependencies(clazz, annotation);
		processAnnotationsForPatterns(clazz, annotation);
		processAnnotationsForSecurityTests(clazz, annotation);
		addClassByTestNames(clazz, annotation.testNames());
	}

	protected void processAnnotationsForMetrics(Class<?> annotatedClass, ArchitectureTest annotation) {
		if (!annotation.omitMetrics()) {
			addClassForMetrics(annotatedClass);
		}
	}

	protected void addClassForMetrics(Class<?> annotatedClass) {
		addToSourceMap("CyclomaticComplexityTest", annotatedClass);
		addToSourceMap("HalsteadComplexityTest", annotatedClass);
		addToSourceMap("MethodLengthTest", annotatedClass);
		addToSourceMap("NumberOfMethodsPerClassTest", annotatedClass);
	}

	protected void processAnnotationsForConventions(Class<?> annotatedClass, ArchitectureTest annotation) {
		if (!annotation.omitConventions()) {
			addClassForConventions(annotatedClass);
		}
	}

	protected void addClassByTestNames(Class<?> annotatedClass, String[] testNames) {
		for (String testName : testNames) {
			addToSourceMap(testName, annotatedClass);
		}
	}

	protected void addClassForConventions(Class<?> annotatedClass) {
		addToSourceMap("CamelCaseTest", annotatedClass);
		addToSourceMap("NoInnerClassesTest", annotatedClass);
		addToSourceMap("OrderingTest", annotatedClass);
		addToSourceMap("OverlyChainedMethodCallsTest", annotatedClass);
		addToSourceMap("NoFinalModifierInLocalVariablesTest", annotatedClass);
	}

	protected void processAnnotationsForDependencies(Class<?> annotatedClass, ArchitectureTest annotation) {
		Dependency[] dependencies = annotation.dependencies();
		addClassForDependencies(annotatedClass, dependencies);
	}

	protected void addClassForDependencies(Class<?> annotatedClass, Dependency[] dependencies) {
		for (Dependency dependency : dependencies) {
			if (dependency.equals(Dependency.acyclicDependenciesPrinciple)) {
				addToSourceMap("AcyclicDependenciesPrincipleTest", annotatedClass);
			}
			if (dependency.equals(Dependency.usesDependency)) {
				addToSourceMap("UsesTest", annotatedClass);
			}
			if (dependency.equals(Dependency.extendsDependency)) {
				addToSourceMap("ExtendsTest", annotatedClass);
			}
			if (dependency.equals(Dependency.implementsDependency)) {
				addToSourceMap("ImplementsTest", annotatedClass);
			}
			if (dependency.equals(Dependency.throwsDependency)) {
				addToSourceMap("ThrowsTest", annotatedClass);
			}
			if (dependency.equals(Dependency.overridesDependency)) {
				addToSourceMap("OverridesTest", annotatedClass);
			}
			if (dependency.equals(Dependency.returnsDependency)) {
				addToSourceMap("ReturnsTest", annotatedClass);
			}
			if (dependency.equals(Dependency.uncheckedCasts)) {
				addToSourceMap("UncheckedCastsTest", annotatedClass);
			}
			if (dependency.equals(Dependency.dangerousCasts)) {
				addToSourceMap("DangerousCastsTest", annotatedClass);
			}
			if (dependency.equals(Dependency.instabilityDependency)) {
				addToSourceMap("InstabilityTest", annotatedClass);
			}
			if (dependency.equals(Dependency.methodPurity)) {
				addToSourceMap("MethodPurityTest", annotatedClass);
			}
			if (dependency.equals(Dependency.annotationTypeDependency)) {
				addToSourceMap("AnnotationTypeTest", annotatedClass);
			}
			if (dependency.equals(Dependency.all)) {
				addToSourceMap("AcyclicDependenciesPrincipleTest", annotatedClass);
				addToSourceMap("UsesTest", annotatedClass);
				addToSourceMap("ExtendsTest", annotatedClass);
				addToSourceMap("ImplementsTest", annotatedClass);
				addToSourceMap("ThrowsTest", annotatedClass);
				addToSourceMap("OverridesTest", annotatedClass);
				addToSourceMap("ReturnsTest", annotatedClass);
				addToSourceMap("UncheckedCastsTest", annotatedClass);
				addToSourceMap("DangerousCastsTest", annotatedClass);
				addToSourceMap("InstabilityTest", annotatedClass);
				addToSourceMap("MethodPurityTest", annotatedClass);
				addToSourceMap("AnnotationTypeTest", annotatedClass);
			}
		}
	}

	protected void processAnnotationsForPatterns(Class<?> annotatedClass, ArchitectureTest annotation) {
		Pattern[] patterns = annotation.patterns();
		addClassForPatterns(annotatedClass, patterns);
	}

	protected void processAnnotationsForSecurityTests(Class<?> annotatedClass, ArchitectureTest annotation) {
		if (annotation.enforceSecurityTests()) {
			addToSourceMap("FileUserRightsTest", annotatedClass);
			addToSourceMap("SecureSqlStatementsTest", annotatedClass);
		}
	}

	protected void addClassForPatterns(Class<?> annotatedClass, Pattern[] patterns) {
		for (Pattern pattern : patterns) {
			if (pattern.equals(Pattern.state)) {
				addToSourceMap("StateTest", annotatedClass);
			}
			if (pattern.equals(Pattern.singleton)) {
				addToSourceMap("SingletonTest", annotatedClass);
			}
			if (pattern.equals(Pattern.strategy)) {
				addToSourceMap("StrategyTest", annotatedClass);
			}
			if (pattern.equals(Pattern.adapter)) {
				addToSourceMap("AdapterTest", annotatedClass);
			}
			if (pattern.equals(Pattern.templateMethod)) {
				addToSourceMap("TemplateMethodTest", annotatedClass);
			}
		}
	}

	private void addToSourceMap(String key, Class<?> value) {
		Exclude exclude = value.getAnnotation(Exclude.class);
		if (exclude != null) {
			Class<?>[] tests = exclude.tests();
			for (Class<?> test : tests) {
				if (test.getSimpleName().equals(key)) {
					return;
				}
			}
		}
		Set<Class<?>> items = sourceMap.get(key);
		if (items == null) {
			items = newHashSet();
		}
		if (!value.equals(Object.class)) {
			items.add(value);
		}
		sourceMap.put(key, items);
	}

}
