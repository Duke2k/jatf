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

package jatf;

import jatf.common.ArchitectureTestDataProvider;
import jatf.common.IArchitectureTest;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.ArchitectureTestRunListener.report;

public abstract class ArchitectureTestBase implements IArchitectureTest {

  protected static final String DATA_PROVIDER_NAME = "provideClassesToTest";

  protected static Properties properties = new Properties();
  private static ArchitectureTestDataProvider dataProvider;

  static {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("jatf-test.properties");
    try {
      properties.load(inputStream);
    } catch (IOException e) {
      report("Could not load jatf-test.properties:", e);
    }
  }

  /**
   * @param test - test class, for which the applicable classes should be returned
   * @return Set of classes for the given test
   */
  @Nonnull
  protected static Set<Class<?>> provideClassesFor(@Nonnull Class<? extends ArchitectureTestBase> test) {
    initializeDataProvider();
    Set<Class<?>> result = dataProvider.getClassesFor(test.getSimpleName());
    if (Objects.isNull(result)) {
      result = newHashSet();
    }
    if (result.isEmpty()) {
      result.add(test);
    }
    return result;
  }

  /**
   * This generic method converts a set of objects into a 2-dimensional array, so that it can be used as data provider.
   *
   * @param items - Set of items to be converted into a 2-dimensional array
   * @return a 2-dimensional Object array from the given items
   */
  @Nonnull
  protected static <T> Object[][] getProvider(@Nonnull Set<T> items) {
    Object[][] result = new Object[items.size()][1];
    int i = 0;
    for (T item : items) {
      result[i++][0] = item;
    }
    return result;
  }

  /**
   * This method initializes the dataProvider field, if necessary.
   */
  private static void initializeDataProvider() {
    if (dataProvider == null) {
      dataProvider = new ArchitectureTestDataProvider();
    }
  }

  /**
   * This generic method returns the annotation determined by its desired type, if any. Here is the point where a
   * rule wins over an actual annotation: First, the rules are queried for possible annotations of the desired type,
   * and if there is none, a possible actual annotation of the desired type is returned.
   *
   * @param clazz                 - the class which is under test
   * @param desiredAnnotationType - the desired annotation type
   * @return the annotation of the desired type, or null if there is none.
   */
  @Nullable
  protected <A extends Annotation> A getAnnotationFor(@Nonnull Class<?> clazz, Class<A> desiredAnnotationType) {
    Annotation annotation = dataProvider.getAnnotationFor(clazz, desiredAnnotationType);
    if (Objects.nonNull(annotation)) {
      //noinspection unchecked
      return (A) annotation;
    }
    return clazz.getAnnotation(desiredAnnotationType);
  }
}
