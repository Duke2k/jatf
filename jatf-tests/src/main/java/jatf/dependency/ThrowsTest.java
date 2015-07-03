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

import jatf.annotations.MustNotThrow;
import jatf.annotations.MustThrow;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class ThrowsTest extends DependencyTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(ThrowsTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testThrows(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(MustThrow.class) != null) {
                for (Class<? extends Throwable> throwable : method.getAnnotation(MustThrow.class).throwables()) {
                    testThrows(method, throwable);
                }
            }
            if (method.getAnnotation(MustNotThrow.class) != null) {
                for (Class<? extends Throwable> throwable : method.getAnnotation(MustNotThrow.class).throwables()) {
                    testNotThrows(method, throwable);
                }
            }
        }
    }

    protected void testThrows(Method method, Class<?> throwableClass) {
        assertTrue(method.getName() + " does not throw " + throwableClass.getName(), findMethod(method, throwableClass));
    }

    protected void testNotThrows(Method method, Class<?> throwableClass) {
        assertFalse(method.getName() + " throws " + throwableClass.getName(), findMethod(method, throwableClass));
    }

    private boolean findMethod(Method method, Class<?> throwableClass) {
        boolean isInThrowables = false;
        for (Class<?> throwable : method.getExceptionTypes()) {
            if (throwable.equals(throwableClass)) {
                isInThrowables = true;
                break;
            }
        }
        return isInThrowables;
    }
}
