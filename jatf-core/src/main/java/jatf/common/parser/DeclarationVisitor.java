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

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class DeclarationVisitor extends VoidVisitorAdapter<Object> {

  private Map<String, Type> typeMap = newHashMap();
  private List<String> typeNames = newArrayList();
  private Map<String, EnumSet<Modifier>> modifiersByName = newHashMap();
  private List<String> typeDeclarations = newArrayList();

  @Override
  public void visit(VariableDeclarationExpr variableDeclarationExpr, Object arguments) {
    for (VariableDeclarator variableDeclarator : variableDeclarationExpr.getVariables()) {
      String varName = variableDeclarator.getNameAsString();
      typeMap.put(varName, variableDeclarationExpr.getElementType());
      typeNames.add(varName);
      modifiersByName.put(varName, variableDeclarationExpr.getModifiers());
    }
  }

  @Override
  public void visit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration, Object arguments) {
    typeDeclarations.add(classOrInterfaceDeclaration.getNameAsString());
  }

  @Nullable
  public Type getTypeBy(@Nonnull String name) {
    return typeMap.get(name);
  }

  @Nonnull
  public List<String> getTypeNames() {
    return typeNames;
  }

  public EnumSet<Modifier> getModifierFor(@Nonnull String name) {
    return modifiersByName.get(name);
  }

  @Nonnull
  public List<String> getTypeDeclarations() {
    return typeDeclarations;
  }
}
