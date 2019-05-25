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

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public abstract class AbstractArchitectureTestEvaluatorTest {

  private static final int NUMBER_OF_DEFAULT_TESTS = 11;

  void verify(Map<String, Set<Class<?>>> targetMap, Class<?>... classes) {
    assertEquals(NUMBER_OF_DEFAULT_TESTS, targetMap.size());
    for (Class<?> clazz : classes) {
      assertThat(targetMap, contains(clazz));
    }
  }

  private TypeSafeMatcher<Map<String, Set<Class<?>>>> contains(Class<?> clazz) {
    return new TypeSafeMatcher<Map<String, Set<Class<?>>>>() {
      @Override
      protected boolean matchesSafely(Map<String, Set<Class<?>>> stringSetMap) {
        for (Set<Class<?>> classesSet : stringSetMap.values()) {
          if (classesSet.contains(clazz)) {
            return true;
          }
        }
        return false;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Class '" + clazz + "' is not present in Map.");
      }
    };
  }
}
