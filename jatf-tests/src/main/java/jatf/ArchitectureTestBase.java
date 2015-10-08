/**
 * This file is part of JATF.
 *
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf;

import jatf.common.ArchitectureTestDataProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public abstract class ArchitectureTestBase {

    protected static final String DATA_PROVIDER_NAME = "provideClassesToTest";
    private static ArchitectureTestDataProvider dataProvider;

    /**
     * This method initializes the dataProvider field, if necessary.
     */
    public static void initializeDataProvider() {
        if (dataProvider == null) {
            dataProvider = new ArchitectureTestDataProvider();
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
        if (result == null) {
            result = newHashSet();
        }
        if (result.size() == 0) {
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
     * This generic method returns the annotation determined by its desired type, if any. Here is the point where a
     * rule wins over an actual annotation: First, the rules are queried for possible annotations of the desired type,
     * and if there is none, a possible actual annotation of the desired type is returned.
     *
     * @param clazz - the class which is under test
     * @param desiredAnnotationType - the desired annotation type
     * @return the annotation of the desired type, or null if there is none.
     */
    @Nullable
    protected <A extends Annotation> A getAnnotationFor(@Nonnull Class<?> clazz, Class<A> desiredAnnotationType) {
        Annotation annotation = dataProvider.getAnnotationFor(clazz, desiredAnnotationType);
        if (annotation != null) {
            //noinspection unchecked
            return (A) annotation;
        }
        return clazz.getAnnotation(desiredAnnotationType);
    }
}
