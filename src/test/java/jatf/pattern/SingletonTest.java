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

import jatf.common.ArchitectureTestRunListener;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class SingletonTest extends PatternTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(SingletonTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testForSingletonPattern(Class<?> clazz) {
        if (clazz.equals(getClass())) {
            return;
        }

        // check if only one constructor is present and that it is private
        try {
            Constructor[] constructors = clazz.getDeclaredConstructors();
            assertEquals(constructors.length, 1);
            assertTrue("Constructor in " + clazz.getName() + " is not private.",
                    Modifier.isPrivate(constructors[0].getModifiers()));

            // check if a method 'getInstance' exists, that it returns an object of this class and that it is public and static
            Method[] methods = clazz.getDeclaredMethods();
            Method getInstanceMethod = findMemberBy("getInstance", methods);
            assertTrue("getInstance() method in " + clazz.getName() + " does not exist.", getInstanceMethod != null);
            assertEquals("getInstance() method in " + clazz.getName() + " does not return this instance.",
                    getInstanceMethod.getReturnType(), clazz);
            assertTrue("getInstance() method in " + clazz.getName() + " is not static.",
                    Modifier.isStatic(getInstanceMethod.getModifiers()));
            assertTrue("getInstance() method in " + clazz.getName() + " is not public.",
                    Modifier.isPublic(getInstanceMethod.getModifiers()));
        } catch (NoClassDefFoundError e) {
            ArchitectureTestRunListener.report("Could not check " + clazz.getName() + " for Singleton pattern properties:", e);
        }
    }
}
