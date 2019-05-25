/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.conventions;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.parser.DeclarationVisitor;
import jatf.common.parser.MethodVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertFalse;

@RunWith(DataProviderRunner.class)
public class NoFinalModifierInLocalVariablesTest extends ConventionsTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(NoFinalModifierInLocalVariablesTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testNoFinalModifierInLocalVariables(Class<?> clazz) {
    MethodVisitor methodVisitor = new MethodVisitor();
    parseWithVoidVisitor(clazz, methodVisitor);
    for (String methodName : methodVisitor.getMethodNames()) {
      BlockStmt body = methodVisitor.getMethodBody(methodName);
      if (body != null) {
        DeclarationVisitor declarationVisitor = new DeclarationVisitor();
        parseWithVoidVisitor(body, declarationVisitor);
        for (String variableName : declarationVisitor.getTypeNames()) {
          assertFalse("Local variable " + variableName + " in method " + methodName + " should not be final",
              declarationVisitor.getModifierFor(variableName).contains(Modifier.FINAL));
        }
      }
      List<Parameter> parameterList = methodVisitor.getParametersOfMethod(methodName);
      if (parameterList != null) {
        for (Parameter parameter : parameterList) {
          if (!isUsedInAnonymousClass(parameter, body)) {
            assertFalse("Method parameter " + parameter.getNameAsString() + " in method " + methodName + " should not be final",
                parameter.getModifiers().contains(Modifier.FINAL));
          }
        }
      }
    }
  }

  private boolean isUsedInAnonymousClass(@Nonnull Parameter parameter, @Nullable BlockStmt body) {
    //noinspection RedundantIfStatement
    if (body != null) {
      // TODO

      return true;
    }
    return false;
  }
}
