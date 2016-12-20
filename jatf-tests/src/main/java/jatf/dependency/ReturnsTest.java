/**
 * This file is part of JATF.
 * <p/>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p/>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.dependency;

import com.google.gson.Gson;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.annotations.MustNotReturn;
import jatf.annotations.MustReturn;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class ReturnsTest extends DependencyTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(ReturnsTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testReturns(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getAnnotation(MustReturn.class) != null) {
                testReturns(method, method.getAnnotation(MustReturn.class).types());
            }
            if (method.getAnnotation(MustNotReturn.class) != null) {
                testNotReturns(method, method.getAnnotation(MustNotReturn.class).types());
            }
        }
    }

    private void testReturns(Method method, Class<?>[] types) {
        assertTrue(method.getName() + " does not return one of" + new Gson().toJson(types), isReturnedBy(method, types));
    }

    private void testNotReturns(Method method, Class<?>[] types) {
        assertFalse(method.getName() + " returns one of" + new Gson().toJson(types), isReturnedBy(method, types));
    }

    private boolean isReturnedBy(@Nonnull Method method, @Nonnull Class<?>[] types) {
        boolean result = false;
        for (Class<?> type : types) {
            if (type.equals(method.getReturnType())) {
                result = true;
                break;
            }
        }
        return result;
    }
}