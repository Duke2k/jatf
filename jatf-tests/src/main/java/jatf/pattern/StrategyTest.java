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

package jatf.pattern;

import com.github.javaparser.ast.stmt.Statement;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.parser.MethodVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

/**
 * The Strategy pattern is used, when it becomes necessary to decouple specific behaviour to be implemented in an
 * inherited method.
 * <p/>
 * For this test, it means that we have to check for a field that inherits from a class or implements an interface
 * that models the behaviour itself. We also have to check for an inherited  method that calls the behaviour through
 * its superclass or interface.
 */
@RunWith(DataProviderRunner.class)
public class StrategyTest extends PatternTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(StrategyTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForStrategyPattern(Class<?> clazz) {
    MethodVisitor methodVisitor = new MethodVisitor();
    parseWithVoidVisitor(clazz, methodVisitor);
    Class<?> superClass = clazz.getSuperclass();
    Field[] fields = superClass.getDeclaredFields();
    Set<Field> foundFields = newHashSet();
    Method[] methods = clazz.getMethods();
    for (Field field : fields) {
      for (Method method : methods) {
        String methodName = method.getName();
        List<Statement> statements = methodVisitor.getStatementsInMethod(methodName);
        if (statements != null) {
          for (Statement statement : statements) {
            if (statement.toString().contains(field.getName())) {
              foundFields.add(field);
              break;
            }
          }
        }
      }
      assertTrue("Field " + field.getName() + " is not found in " + clazz.getName(),
          foundFields.contains(field));
    }
  }
}
