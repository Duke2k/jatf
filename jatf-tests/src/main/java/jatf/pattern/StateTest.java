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
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * The test here has known weaknesses: It assumes that an enum field is automatically a representation of the class'
 * states, which may or may not be the case. It then checks if all states within that enum get set in the class'
 * methods.
 */
@RunWith(DataProviderRunner.class)
public class StateTest extends PatternTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(StateTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForStatePattern(Class<?> clazz) {
    if (clazz.equals(getClass())) {
      return;
    }
    Field[] fields = clazz.getDeclaredFields();
    Object[] states = new Object[0];
    Field stateField = null;
    for (Field field : fields) {
      if (field.getType().isEnum()) {
        states = field.getType().getEnumConstants();
        stateField = field;
        break;
      }
    }
    assertTrue(states.length > 0);
    assertTrue(stateField != null);
    assertTrue(Modifier.isPrivate(stateField.getModifiers()));
    assertFalse(Modifier.isStatic(stateField.getModifiers()));
    Method[] methods = clazz.getMethods();
    MethodVisitor methodVisitor = new MethodVisitor();
    parseWithVoidVisitor(clazz, methodVisitor);
    Map<String, Boolean> areAllStatesSet = newHashMap();
    for (Object state : states) {
      String stateRepresentation = state.toString();
      areAllStatesSet.put(stateRepresentation, false);
      for (Method method : methods) {
        List<Statement> methodStatements = methodVisitor.getStatementsInMethod(method.getName());
        if (methodStatements != null) {
          for (Statement statement : methodStatements) {
            String line = statement.toString().trim();
            if (line.contains(stateField.getName()) && line.contains(stateRepresentation)) {
              areAllStatesSet.put(stateRepresentation, true);
            }
          }
        }
      }
    }
    for (String state : areAllStatesSet.keySet()) {
      assertTrue("Not all states (" + state + ") are set in class " + clazz.getName(), areAllStatesSet.get(state));
    }
  }
}
