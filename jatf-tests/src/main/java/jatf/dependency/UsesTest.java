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

package jatf.dependency;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.annotations.MustNotUse;
import jatf.annotations.MustUse;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.util.Set;

import static jatf.common.util.ArchitectureTestUtil.assertMessage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class UsesTest extends DependencyTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(UsesTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testUses(Class<?> clazz) {
    MustUse mustUse = getAnnotationFor(clazz, MustUse.class);
    if (mustUse != null) {
      for (Class<?> toBeUsed : mustUse.types()) {
        testUses(clazz, toBeUsed);
      }
    }
    MustNotUse mustNotUse = getAnnotationFor(clazz, MustNotUse.class);
    if (mustNotUse != null) {
      for (Class<?> notToBeUsed : mustNotUse.types()) {
        testDoesNotUse(clazz, notToBeUsed);
      }
    }
  }

  @SuppressWarnings("WeakerAccess")
  protected void testUses(Class<?> clazz, Class<?> toBeUsed) {
    assertTrue(assertMessage(clazz, toBeUsed + " not used."), isUsed(clazz, toBeUsed));
  }

  @SuppressWarnings("WeakerAccess")
  protected void testDoesNotUse(Class<?> clazz, Class<?> notToBeUsed) {
    assertFalse(assertMessage(clazz, notToBeUsed + " used."), isUsed(clazz, notToBeUsed));
  }

  private boolean isUsed(Class<?> clazz, Class<?> toBeUsed) {
    for (Field field : clazz.getDeclaredFields()) {
      if (field.getType().equals(toBeUsed)) {
        return true;
      }
    }
    return false;
  }
}
