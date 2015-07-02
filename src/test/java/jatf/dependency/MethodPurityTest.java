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

import jatf.annotations.MustBePure;
import jatf.common.parser.AssignmentVisitor;
import jatf.common.parser.MethodCallVisitor;
import jatf.common.parser.MethodVisitor;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.stmt.BlockStmt;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.ArchitectureTestRunListener.report;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class MethodPurityTest extends DependencyTestBase {

    private Set<String> visitedMethods;
    private MethodVisitor methodVisitor;
    private Field[] declaredFields;

    private int impurityCount;
    private int assignmentsCount;
    private int methodCallsCount;

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(MethodPurityTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testMethods(Class<?> clazz) {
        visitedMethods = newHashSet();
        methodVisitor = new MethodVisitor();
        parseWithVoidVisitor(clazz, methodVisitor);
        try {
            declaredFields = clazz.getDeclaredFields();
            for (Method method : clazz.getMethods()) {
                impurityCount = 0;
                assignmentsCount = 0;
                methodCallsCount = 0;
                MustBePure mustBePure = method.getAnnotation(MustBePure.class);
                if (mustBePure != null) {
                    List<Parameter> parameters = methodVisitor.getParametersOfMethod(method.getName());
                    BlockStmt body = methodVisitor.getMethodBody(method.getName());
                    if (body != null) {
                        testMethod(method.getName(), parameters, body);
                    }
                    double currentDegree;
                    if (assignmentsCount + methodCallsCount == 0) {
                        currentDegree = 1.0;
                    } else {
                        currentDegree = 1.0 - (((double) impurityCount) / (double) (assignmentsCount + methodCallsCount));
                    }
                    double requiredDegree = mustBePure.degree();
                    assertTrue("Required method purity of " + requiredDegree + " not met: " + currentDegree +
                            " in " + method.getName() + " of " + clazz.getName(), currentDegree >= requiredDegree);
                }
            }
        } catch (NoClassDefFoundError e) {
            report("Could not completely check for method purity in " + clazz.getName(), e);
        }
    }

    private void testMethod(@Nonnull String methodName, @Nullable List<Parameter> parameters, @Nonnull BlockStmt body) {
        if (!visitedMethods.contains(methodName)) {
            visitedMethods.add(methodName);
            MethodCallVisitor methodCallVisitor = new MethodCallVisitor();
            parseWithVoidVisitor(body, methodCallVisitor);
            for (String calledMethodName : methodCallVisitor.getMethodCalls().keySet()) {
                methodCallsCount++;
                List<Parameter> calledMethodParameters = methodVisitor.getParametersOfMethod(calledMethodName);
                BlockStmt calledMethodBody = methodVisitor.getMethodBody(calledMethodName);
                if (calledMethodParameters != null && calledMethodBody != null && !visitedMethods.contains(calledMethodName)) {
                    testMethod(calledMethodName, calledMethodParameters, calledMethodBody);
                }
            }
            if (parameters != null) {
                AssignmentVisitor assignmentVisitor = new AssignmentVisitor();
                parseWithVoidVisitor(body, assignmentVisitor);
                for (Parameter parameter : parameters) {
                    VariableDeclaratorId paramId = parameter.getId();
                    String paramName = paramId.getName();
                    for (AssignmentVisitor.Assignment assignment : assignmentVisitor.getAssignments()) {
                        assignmentsCount++;
                        Expression target = assignment.getTarget();
                        String targetName = target.toString().trim();
                        if (targetName.equals(paramName)) {
                            impurityCount++;
                        }
                        for (Field field : declaredFields) {
                            if (targetName.equals(field.getName())) {
                                impurityCount++;
                            }
                        }
                    }
                }
            }
        }
    }
}