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

package jatf.common.rules;

import static jatf.common.util.ArchitectureTestUtil.buildReflections;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;

import jatf.annotations.ArchitectureTest;
import jatf.common.rules.markers.MustExtendMarker;
import jatf.common.rules.markers.MustImplementMarker;
import jatf.common.rules.markers.MustNotExtendMarker;
import jatf.common.rules.markers.MustNotImplementMarker;
import jatf.common.rules.markers.MustNotUseMarker;
import jatf.common.rules.markers.MustUseMarker;

public class RuleTest {

	private static Class<?>[] classes;

	@BeforeClass
	public static void prepareClasses() {
		Reflections reflections = buildReflections();
		Set<Class<?>> classesSet = reflections.getTypesAnnotatedWith(ArchitectureTest.class);
		classes = new Class[classesSet.size()];
		Iterator<Class<?>> classesIterator = classesSet.iterator();
		for (int i = 0; i < classesSet.size(); i++) {
			classes[i] = classesIterator.next();
		}
	}

	@Test
	public void mustImplementRule() {
		MustImplementMarker marker = new MustImplementMarker();
		MustImplementRule rule = new MustImplementRule(classes, marker);
		Class<?>[] interfaces = rule.interfaces();
		assertEquals(interfaces.length, 0);
	}

	@Test
	public void mustNotImplementRule() {
		MustNotImplementMarker marker = new MustNotImplementMarker();
		MustNotImplementRule rule = new MustNotImplementRule(classes, marker);
		Class<?>[] interfaces = rule.interfaces();
		assertEquals(interfaces.length, 0);

	}

	@Test
	public void mustExtendRule() {
		MustExtendMarker marker = new MustExtendMarker();
		MustExtendRule rule = new MustExtendRule(classes, marker);
		Class<?> type = rule.type();
		assertEquals(type, Object.class);
	}

	@Test
	public void mustNotExtendRule() {
		MustNotExtendMarker marker = new MustNotExtendMarker();
		MustNotExtendRule rule = new MustNotExtendRule(classes, marker);
		Class<?> type = rule.type();
		assertEquals(type, Object.class);
	}

	@Test
	public void mustUseRule() {
		MustUseMarker marker = new MustUseMarker();
		MustUseRule rule = new MustUseRule(classes, marker);
		Class<?>[] types = rule.types();
		assertEquals(types.length, 0);
	}

	@Test
	public void mustNotUseRule() {
		MustNotUseMarker marker = new MustNotUseMarker();
		MustNotUseRule rule = new MustNotUseRule(classes, marker);
		Class<?>[] types = rule.types();
		assertEquals(types.length, 0);
	}
}
