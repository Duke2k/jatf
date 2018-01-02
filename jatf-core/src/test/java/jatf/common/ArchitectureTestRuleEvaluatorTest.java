/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common;

import static com.google.common.collect.Maps.newHashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.Test;
import org.reflections.Reflections;

import jatf.annotations.MustExtend;
import jatf.api.rules.ArchitectureTestRuleGenerator;
import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.rules.ArchitectureTestRule;
import jatf.common.rules.conditions.AlwaysTrue;
import jatf.common.rules.markers.ArchitectureTestMarker;
import jatf.common.rules.markers.RuleBasedMarker;

public class ArchitectureTestRuleEvaluatorTest extends AbstractArchitectureTestEvaluatorTest {

	@Test
	public void evaluateInto_Mocked() {

		// prepare
		Reflections reflections = mock(Reflections.class);
		when(reflections.getSubTypesOf(ArchitectureTestRuleGenerator.class)).thenReturn(Collections.singleton(TestRuleGenerator.class));
		ArchitectureTestRuleEvaluator evaluator = new ArchitectureTestRuleEvaluator(reflections);
		Map<String, Set<Class<?>>> targetMap = newHashMap();

		// test
		evaluator.evaluateInto(targetMap);

		// verify
		verify(targetMap, TestClass.class);
	}

	static class TestClass extends TestExtendClass {
	}

	static abstract class TestExtendClass {
	}

	static class TestClassMarker extends RuleBasedMarker<MustExtend> {
		@Override
		public Class<MustExtend> annotationType() {
			return MustExtend.class;
		}

		@Override
		public MustExtend createAnnotation() {
			return new MustExtend() {

				@Override
				public String toString() {
					return annotationType().getSimpleName();
				}

				@Override
				public Class<? extends Annotation> annotationType() {
					return MustExtend.class;
				}

				@Override
				public Class<?> type() {
					return TestExtendClass.class;
				}
			};
		}
	}

	static class TestRule extends ClassAnnotationBasedRule<TestClassMarker, AlwaysTrue> {
		TestRule(Class<?>[] classes, TestClassMarker marker) {
			super(classes, new AlwaysTrue(), marker);
		}
	}

	static class TestRuleGenerator extends ArchitectureTestRuleGenerator<TestClassMarker, TestRule> {

		@Nonnull
		@Override
		public TestRule generateRule() {
			return new TestRule(new Class[]{TestClass.class}, new TestClassMarker());
		}

		@Nonnull
		@Override
		public ArchitectureTestRule generateArchitectureTestRuleFor(@Nonnull Class<?>[] classes) {
			return new ArchitectureTestRule(new Class[]{TestClass.class}, new ArchitectureTestMarker());
		}
	}
}
