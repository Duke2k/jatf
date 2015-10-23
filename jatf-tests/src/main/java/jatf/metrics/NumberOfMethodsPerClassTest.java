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

package jatf.metrics;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.parser.MethodVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static jatf.common.ArchitectureTestConstraints.MAX_NUMBER_OF_METHODS_PER_CLASS;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class NumberOfMethodsPerClassTest extends MetricsTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(NumberOfMethodsPerClassTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testNumberOfMethods(Class<?> clazz) {
        MethodVisitor methodVisitor = new MethodVisitor();
        parseWithVoidVisitor(clazz, methodVisitor);
        int methodCount = methodVisitor.getMethodNames().size();
        assertTrue("Number of methods threshold violated in " + clazz.getName() + ": " + methodCount + " methods.",
                methodCount <= MAX_NUMBER_OF_METHODS_PER_CLASS);
    }
}
