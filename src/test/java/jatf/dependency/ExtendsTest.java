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

package jatf.dependency;

import jatf.annotations.MustExtend;
import jatf.annotations.MustNotExtend;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class ExtendsTest extends DependencyTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(ExtendsTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testExtends(Class<?> clazz) {
        MustExtend mustExtendAnnotation = getAnnotationFor(clazz, MustExtend.class);
        MustNotExtend mustNotExtendAnnotation = getAnnotationFor(clazz, MustNotExtend.class);
        Class<?> mustExtend = null;
        Class<?> mustNotExtend = null;
        if (mustExtendAnnotation != null) {
            mustExtend = mustExtendAnnotation.type();
        }
        if (mustNotExtendAnnotation != null) {
            mustNotExtend = mustNotExtendAnnotation.type();
        }
        if (mustExtend != null && mustNotExtend != null) {
            assertFalse("The same type is provided in both @MustExtend and @MustNotExtend.", mustExtend.equals(mustNotExtend));
        }
        if (mustExtend != null && !mustExtend.equals(Object.class)) {
            testExtends(clazz, mustExtend);
        }
        if (mustNotExtend != null && !mustNotExtend.equals(Object.class)) {
            testDoesNotExtend(clazz, mustNotExtend);
        }
    }

    protected void testDoesNotExtend(Class<?> clazz, Class<?> classNotToExtend) {
        assertFalse(clazz.getSuperclass().equals(classNotToExtend));
    }

    protected void testExtends(Class<?> clazz, Class<?> classToExtend) {
        assertTrue(clazz.getSuperclass().equals(classToExtend));
    }
}
