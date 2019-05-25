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

package jatf.dependency;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.annotations.MustHaveAnnotation;
import jatf.annotations.MustNotHaveAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Annotation;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class AnnotationTypeTest extends DependencyTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(AnnotationTypeTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void isAnnotated(Class<?> clazz) {
    Annotation[] annotations = clazz.getAnnotations();
    for (Annotation annotation : annotations) {
      if (annotation.annotationType().equals(MustHaveAnnotation.class)) {
        assertTrue("Required annotation " + annotation + " not present in " + clazz.getName(),
            checkIfAnnotationIsPresentIn(clazz, ((MustHaveAnnotation) annotation).annotation()));
      }
      if (annotation.annotationType().equals(MustNotHaveAnnotation.class)) {
        assertFalse("Forbidden annotation " + annotation + " present in " + clazz.getName(),
            checkIfAnnotationIsPresentIn(clazz, ((MustNotHaveAnnotation) annotation).annotation()));
      }
    }
  }

  private boolean checkIfAnnotationIsPresentIn(Class<?> clazz, Class<? extends Annotation> annotation) {
    Annotation[] annotations = clazz.getAnnotations();
    for (Annotation a : annotations) {
      if (a.annotationType().equals(annotation)) {
        return true;
      }
    }
    return false;
  }
}
