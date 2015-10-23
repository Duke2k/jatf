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

package jatf.conventions;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.Statement;
import jatf.common.parser.MethodCallVisitor;
import jatf.common.parser.MethodVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static jatf.common.ArchitectureTestConstraints.MAX_CHAINED_METHOD_CALLS;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class OverlyChainedMethodCallsTest extends ConventionsTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(OverlyChainedMethodCallsTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testIfOverlyChainedMethodCallsExist(Class<?> clazz) {
        MethodVisitor methodVisitor = new MethodVisitor();
        parseWithVoidVisitor(clazz, methodVisitor);
        for (String methodName : methodVisitor.getMethodNames()) {
            BlockStmt body = methodVisitor.getMethodBody(methodName);
            if (body != null) {
                MethodCallVisitor methodCallVisitor = new MethodCallVisitor();
                parseWithVoidVisitor(body, methodCallVisitor);
                for (String calledMethodName : methodCallVisitor.getMethodCalls().keySet()) {
                    List<Statement> statementsToExamine = methodVisitor.getStatementsInMethod(methodName);
                    if (statementsToExamine != null) {
                        for (Statement statement : statementsToExamine) {
                            if (statement instanceof ExpressionStmt && statement.toString().contains(calledMethodName)) {
                                String[] tokens = statement.toString().split(" ");
                                for (String token : tokens) {
                                    assertTrue("Found chained method call in " + clazz.getName() + "." +
                                                    methodName + ": " + statement.toString(),
                                            countDotsWithoutWithIn(token) < MAX_CHAINED_METHOD_CALLS);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int countDotsWithoutWithIn(@Nonnull String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '.') {
                result++;
            }
            if (s.substring(i).startsWith(".with") || s.substring(i).startsWith(".class")) {
                result--;
            }
        }
        return result;
    }
}
