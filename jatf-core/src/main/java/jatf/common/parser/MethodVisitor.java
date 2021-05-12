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

package jatf.common.parser;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class MethodVisitor extends VoidVisitorAdapter<Object> {

  private final Map<String, List<Statement>> statementsByMethodName = newHashMap();
  private final Map<String, List<Parameter>> parametersByMethodName = newHashMap();
  private final Map<String, BlockStmt> methodBodyByName = newHashMap();
  private final List<String> methodNames = newArrayList();
  private final Map<String, NodeList<Modifier>> modifiersByName = newHashMap();
  private final Map<String, Position> positionsByName = newHashMap();

  private boolean sorted = false;

  @Override
  public void visit(MethodDeclaration methodDeclaration, Object arguments) {
    methodNames.add(methodDeclaration.getName().asString());
    if (methodDeclaration.getBegin().isPresent()) {
      positionsByName.put(methodDeclaration.getName().asString(), methodDeclaration.getBegin().get());
    }
    modifiersByName.put(methodDeclaration.getName().asString(), methodDeclaration.getModifiers());
    if (methodDeclaration.getBody() != null && methodDeclaration.getBody().isPresent()) {
      statementsByMethodName.put(methodDeclaration.getName().asString(), methodDeclaration.getBody().get().getStatements());
    }
    parametersByMethodName.put(methodDeclaration.getName().asString(), methodDeclaration.getParameters());
    methodBodyByName.put(methodDeclaration.getName().asString(), methodDeclaration.getBody().get());
  }

  @Nullable
  public List<Statement> getStatementsInMethod(@Nonnull String methodName) {
    return statementsByMethodName.get(methodName);
  }

  @Nullable
  public List<Parameter> getParametersOfMethod(@Nonnull String methodName) {
    return parametersByMethodName.get(methodName);
  }

  @Nullable
  public BlockStmt getMethodBody(@Nonnull String methodName) {
    return methodBodyByName.get(methodName);
  }

  @Nonnull
  public List<String> getMethodNames() {
    if (!sorted) {
      methodNames.sort(Comparator.comparing(methodName -> positionsByName.get(methodName)));
      sorted = true;
    }
    return methodNames;
  }

  public NodeList<Modifier> getModifierFor(@Nonnull String name) {
    return modifiersByName.get(name);
  }
}
