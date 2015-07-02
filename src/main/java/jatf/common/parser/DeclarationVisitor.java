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

package jatf.common.parser;

import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.TypeDeclarationStmt;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class DeclarationVisitor extends VoidVisitorAdapter<Object> {

    private Map<String, TypeDeclaration> typeDeclarationMap = newHashMap();
    private Map<String, Type> typeMap = newHashMap();
    private List<String> typeNames = newArrayList();
    private Map<String, Integer> modifiersByName = newHashMap();

    @Override
    public void visit(TypeDeclarationStmt typeDeclarationStmt, Object arguments) {
        TypeDeclaration typeDeclaration = typeDeclarationStmt.getTypeDeclaration();
        typeDeclarationMap.put(typeDeclaration.getName(), typeDeclaration);
    }

    @Override
    public void visit(VariableDeclarationExpr variableDeclarationExpr, Object arguments) {
        for (VariableDeclarator variableDeclarator : variableDeclarationExpr.getVars()) {
            String varName = variableDeclarator.getId().toString();
            typeMap.put(varName, variableDeclarationExpr.getType());
            typeNames.add(varName);
            modifiersByName.put(varName, variableDeclarationExpr.getModifiers());
        }
    }

    @Nullable
    public Type getTypeBy(@Nonnull String name) {
        return typeMap.get(name);
    }

    @Nonnull
    public Collection<TypeDeclaration> getTypeDeclarations() {
        return typeDeclarationMap.values();
    }

    @Nonnull
    public List<String> getTypeNames() {
        return typeNames;
    }

    public int getModifierFor(@Nonnull String name) {
        return modifiersByName.get(name);
    }
}
