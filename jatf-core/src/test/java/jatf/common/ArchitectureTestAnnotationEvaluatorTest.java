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
import static jatf.common.util.ArchitectureTestUtil.buildReflections;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;

import jatf.annotations.ArchitectureTest;

public class ArchitectureTestAnnotationEvaluatorTest extends AbstractArchitectureTestEvaluatorTest {

	@Test
	public void evaluateInto_Real() {
		// prepare
		Reflections reflections = buildReflections();
		ArchitectureTestAnnotationEvaluator evaluator = new ArchitectureTestAnnotationEvaluator(reflections);
		Map<String, Set<Class<?>>> targetMap = newHashMap();

		// test
		evaluator.evaluateInto(targetMap);

		// verify
		verify(targetMap, TestClass1.class, TestClass2.class);
	}

	@Test
	public void evaluateInto_Mocked() {
		// prepare
		Reflections reflections = mock(Reflections.class);
		Set<Class<?>> classes = new HashSet<>(2);
		classes.add(TestClass1.class);
		classes.add(TestClass2.class);
		when(reflections.getTypesAnnotatedWith(ArchitectureTest.class)).thenReturn(classes);
		ArchitectureTestAnnotationEvaluator evaluator = new ArchitectureTestAnnotationEvaluator(reflections);
		Map<String, Set<Class<?>>> targetMap = newHashMap();

		// test
		evaluator.evaluateInto(targetMap);

		// verify
		verify(targetMap);
	}

	@ArchitectureTest
	private static class TestClass1 {
	}

	@ArchitectureTest
	private static class TestClass2 {
	}
}
