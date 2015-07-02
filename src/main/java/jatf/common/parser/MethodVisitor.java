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

import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class MethodVisitor extends VoidVisitorAdapter<Object> {

    private Map<String, List<Statement>> statementsByMethodName = newHashMap();
    private Map<String, List<Parameter>> parametersByMethodName = newHashMap();
    private Map<String, BlockStmt> methodBodyByName = newHashMap();
    private List<String> methodNames = newArrayList();
    private Map<String, Integer> modifiersByName = newHashMap();
    private Map<String, Integer> positionsByName = newHashMap();

    private boolean sorted = false;

    @Override
    public void visit(MethodDeclaration methodDeclaration, Object arguments) {
        methodNames.add(methodDeclaration.getName());
        positionsByName.put(methodDeclaration.getName(), methodDeclaration.getBeginLine());
        modifiersByName.put(methodDeclaration.getName(), methodDeclaration.getModifiers());
        if (methodDeclaration.getBody() != null && methodDeclaration.getBody().getStmts() != null) {
            statementsByMethodName.put(methodDeclaration.getName(), methodDeclaration.getBody().getStmts());
        }
        parametersByMethodName.put(methodDeclaration.getName(), methodDeclaration.getParameters());
        methodBodyByName.put(methodDeclaration.getName(), methodDeclaration.getBody());
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
            Collections.sort(methodNames, new Comparator<String>() {

                @Override
                public int compare(String method1Name, String method2Name) {
                    return positionsByName.get(method1Name).compareTo(positionsByName.get(method2Name));
                }
            });
            sorted = true;
        }
        return methodNames;
    }

    public int getModifierFor(@Nonnull String name) {
        return modifiersByName.get(name);
    }
}
