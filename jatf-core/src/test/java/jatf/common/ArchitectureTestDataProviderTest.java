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

package jatf.common;

import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertNull;

public class ArchitectureTestDataProviderTest {

    @Test
    public void testDataProvider_Valid() {
        ArchitectureTestDataProvider dataProvider = new ArchitectureTestDataProvider();
        Set<String> testNames = newHashSet();
        fillTestNames(testNames);
        for (String testName : testNames) {
            Set<Class<?>> classes = dataProvider.getClassesFor(testName);
            assertNull(classes);
        }
    }

    private void fillTestNames(@Nonnull Set<String> testNames) {
        testNames.add("CamelCaseTest");
        testNames.add("NoFinalModifierInLocalVariablesTest");
        testNames.add("NoInnerClassesTest");
        testNames.add("OrderingTest");
        testNames.add("OverlyChainedMethodCallsTest");
        testNames.add("AcyclicDependenciesPrincipleTest");
        testNames.add("AnnotationTypeTest");
        testNames.add("DangerousCastsTest");
        testNames.add("ExtendsTest");
        testNames.add("ImplementsTest");
        testNames.add("InstabilityTest");
        testNames.add("MethodPurityTest");
        testNames.add("OverridesTest");
        testNames.add("ReturnsTest");
        testNames.add("ThrowsTest");
        testNames.add("UncheckedCastsTest");
        testNames.add("UsesTest");
        testNames.add("CyclomaticComplexityTest");
        testNames.add("HalsteadComplecityTest");
        testNames.add("MethodLengthTest");
        testNames.add("NumberOfMethodsPerClassTest");
        testNames.add("AdapterTest");
        testNames.add("SingletonTest");
        testNames.add("StateTest");
        testNames.add("StrategyTest");
        testNames.add("TemplateMethodTest");
    }
}