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

import jatf.annotations.MustNotOverride;
import jatf.annotations.MustOverride;
import jatf.common.parser.MethodVisitor;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class OverridesTest extends DependencyTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(OverridesTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testOverrides(Class<?> clazz) {
        MustOverride mustOverride = getAnnotationFor(clazz, MustOverride.class);
        if (mustOverride != null) {
            MethodVisitor methodVisitor = new MethodVisitor();
            parseWithVoidVisitor(clazz, methodVisitor);
            List<String> foundMethodNames = methodVisitor.getMethodNames();
            for (String methodName : mustOverride.methodNames()) {
                assertTrue("Method " + methodName + " must be overridden in " + clazz,
                        foundMethodNames.contains(methodName));
            }
        }
        MustNotOverride mustNotOverride = getAnnotationFor(clazz, MustNotOverride.class);
        if (mustNotOverride != null) {
            MethodVisitor methodVisitor = new MethodVisitor();
            parseWithVoidVisitor(clazz, methodVisitor);
            List<String> foundMethodNames = methodVisitor.getMethodNames();
            for (String methodName : mustNotOverride.methodNames()) {
                assertFalse("Method " + methodName + " must not be overridden in " + clazz,
                        foundMethodNames.contains(methodName));
            }
        }
    }
}
