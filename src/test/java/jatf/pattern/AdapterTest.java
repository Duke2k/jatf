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

package jatf.pattern;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Generally, the Adapter pattern knows two types of adapter (or wrapper) types: class adapters and object adapters.
 * In Java, due to the lack of multiple polymorphism, we can only check for object adapter type pattern.
 * Be aware to apply the @ArchitectureTest(pattern = adapter, ...) annotation to the adapter class, NOT the interface!
 */
@RunWith(DataProviderRunner.class)
public class AdapterTest extends PatternTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(AdapterTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testForAdapterPattern(Class<?> clazz) {
        Class<?>[] interfaces = clazz.getInterfaces();
        Method[] methods = clazz.getMethods();
        for (Class<?> in : interfaces) {
            Method[] methodsInInterface = in.getMethods();
            for (Method methodInInterface : methodsInInterface) {
                Class<?> returnType = methodInInterface.getReturnType();
                Method methodInClass = findMemberBy(methodInInterface.getName(), methods);
                assertTrue("Method not present in " + clazz.getName(), methodInClass != null);
                assertTrue("Method does not have correct return type: " + clazz.getName() + "." + methodInClass.getName(),
                        returnType.equals(methodInClass.getReturnType()));
            }
        }
    }
}
