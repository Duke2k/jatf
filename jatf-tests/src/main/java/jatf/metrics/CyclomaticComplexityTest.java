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
import japa.parser.ast.stmt.Statement;
import jatf.common.parser.MethodVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static jatf.common.ArchitectureTestConstraints.MAXIMUM_CCN;
import static jatf.common.util.ArchitectureTestUtil.findSourceFileFor;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

/**
 * CCN is also known as McCabe Metric. It is simply counting 'if', 'for', 'while' statements etc. in a method.
 * Whenever the control flow of a method splits, the CCN value gets incremented by one.
 */
@RunWith(DataProviderRunner.class)
public class CyclomaticComplexityTest extends MetricsTestBase {

    private final List<String> keywords = Arrays.asList("if", "for", "while", "case", "catch", "&&", "||", "?");

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(CyclomaticComplexityTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testCyclomaticComplexity(Class<?> clazz) {
        File sourceFile = findSourceFileFor(clazz);
        if (sourceFile != null) {
            MethodVisitor methodVisitor = new MethodVisitor();
            parseWithVoidVisitor(clazz, methodVisitor);
            for (String methodName : methodVisitor.getMethodNames()) {
                int currentCcn = 1;
                List<Statement> statements = methodVisitor.getStatementsInMethod(methodName);
                if (statements != null && !statements.isEmpty()) {
                    for (Statement statement : statements) {
                        for (String keyword : keywords) {
                            if (statement.toString().contains(keyword)) {
                                currentCcn++;
                            }
                        }
                    }
                }
                assertTrue("Cyclomatic complexity threshold violated in " +
                        methodName + " of " + clazz.getName(), currentCcn <= MAXIMUM_CCN);
            }
        }
    }
}
