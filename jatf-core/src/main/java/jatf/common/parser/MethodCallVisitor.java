/**
 * This file is part of JATF.
 * <p>
 * JATF is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * <p>
 * JATF is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.parser;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class MethodCallVisitor extends VoidVisitorAdapter<Object> {

  private Map<String, List<Expression>> methodCalls = newHashMap();

  @Override
  public void visit(MethodCallExpr methodCallExpr, Object arguments) {
    methodCalls.put(methodCallExpr.getName().asString(), methodCallExpr.getArguments());
  }

  @Nonnull
  public Map<String, List<Expression>> getMethodCalls() {
    return methodCalls;
  }
}
