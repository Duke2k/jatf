/*
  This file is part of JATF.
  <p/>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p/>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p/>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Set;

import static jatf.api.tests.TestNamesHelper.getTestNames;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ArchitectureTestDataProviderTest {

  @Test
  public void getClassesFor_DefaultTestNames() {
    // prepare
    ArchitectureTestDataProvider dataProvider = new ArchitectureTestDataProvider();
    Set<String> testNames = getTestNames();
    for (String testName : testNames) {

      // test
      Set<Class<?>> classes = dataProvider.getClassesFor(testName);
      // Possible cross-dependency from other tests: We have 3 inner classes to be considered; ArchitectureTestDataProvider
      // can't be mocked appropriately.
      if (classes != null) {

        // verify
        assertEquals(3, classes.size());
        assertThat(classes, containsClassNamed("TestClass"));
        assertThat(classes, containsClassNamed("TestClass1"));
        assertThat(classes, containsClassNamed("TestClass2"));
      }
    }
  }

  private TypeSafeMatcher<Set<Class<?>>> containsClassNamed(String className) {
    return new TypeSafeMatcher<Set<Class<?>>>() {
      @Override
      protected boolean matchesSafely(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
          if (clazz.getSimpleName().equalsIgnoreCase(className)) {
            return true;
          }
        }
        return false;
      }

      @Override
      public void describeTo(Description description) {
        description.appendText("Class named '" + className + "' not present.");
      }
    };
  }
}
