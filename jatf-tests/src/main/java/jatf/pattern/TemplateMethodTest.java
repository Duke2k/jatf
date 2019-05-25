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

package jatf.pattern;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test checks if methods in the abstract super class are abstract there, and implemented in the class under test.
 * This means that the @ArchitectureTest(pattern = templateMethod, ...) annotation is to be applied to the concrete
 * class inheriting from an abstract super class.
 */
@RunWith(DataProviderRunner.class)
public class TemplateMethodTest extends PatternTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(TemplateMethodTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForTemplateMethodPattern(Class<?> clazz) {
    Method[] methodsInConcreteClass = clazz.getMethods();
    Class<?> superClass = clazz.getSuperclass();
    assertTrue("No abstract super class present for " + clazz.getName(), Modifier.isAbstract(superClass.getModifiers()));
    Method[] methodsInAbstractClass = superClass.getMethods();
    for (Method method : methodsInAbstractClass) {
      int modifiers = method.getModifiers();
      if (Modifier.isAbstract(modifiers) && Modifier.isPublic(modifiers)) {
        Method correspondingConcreteMethod = findMemberBy(method.getName(), methodsInConcreteClass);
        assertTrue("No corresponding concrete method for " + method.getName() + " present in " + clazz.getName(),
            correspondingConcreteMethod != null);
        assertTrue("Concrete method " + correspondingConcreteMethod.getName() + " is not public",
            Modifier.isPublic(correspondingConcreteMethod.getModifiers()));
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        assertEquals("Return types of method " + method + " and its inheritance(s) do not match",
            returnType, correspondingConcreteMethod.getReturnType());
        Class<?>[] concreteMethodParameterTypes = correspondingConcreteMethod.getParameterTypes();
        if (parameterTypes.length == concreteMethodParameterTypes.length) {
          for (int i = 0; i < parameterTypes.length; i++) {
            assertEquals("Parameter types of method " + method + " and its inheritance(s) do not match",
                parameterTypes[i], concreteMethodParameterTypes[i]);
          }
        }
      }
    }
  }
}
