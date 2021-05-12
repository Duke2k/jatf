/*
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

package jatf.dependency;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.annotations.MustImplement;
import jatf.annotations.MustNotImplement;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class ImplementsTest extends DependencyTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(ImplementsTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testImplements(Class<?> clazz) {
    MustImplement mustImplement = getAnnotationFor(clazz, MustImplement.class);
    if (mustImplement != null) {
      for (Class<?> in : mustImplement.interfaces()) {
        testImplements(clazz, in);
      }
    }
    MustNotImplement mustNotImplement = getAnnotationFor(clazz, MustNotImplement.class);
    if (mustNotImplement != null) {
      for (Class<?> in : mustNotImplement.interfaces()) {
        testNotImplements(clazz, in);
      }
    }
  }

  @SuppressWarnings("WeakerAccess")
  protected void testNotImplements(Class<?> clazz, Class<?> in) {
    assertFalse("Class " + clazz.getName() + " implements " + in.getName(), isImplemented(clazz, in));
  }

  @SuppressWarnings("WeakerAccess")
  protected void testImplements(Class<?> clazz, Class<?> in) {
    assertTrue("Class " + clazz.getName() + " does not implement " + in.getName(), isImplemented(clazz, in));
  }

  private boolean isImplemented(Class<?> clazz, Class<?> interfaceToBeImplemented) {
    boolean interfaceImplemented = false;
    for (Class<?> in : clazz.getInterfaces()) {
      if (in.equals(interfaceToBeImplemented)) {
        interfaceImplemented = true;
        break;
      }
    }
    return interfaceImplemented;
  }
}
