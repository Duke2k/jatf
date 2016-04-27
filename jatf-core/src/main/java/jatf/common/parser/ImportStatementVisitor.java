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

import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.expr.QualifiedNameExpr;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class ImportStatementVisitor extends VoidVisitorAdapter<Object> {

    private Set<String> importStatements = newHashSet();

    @Override
    public void visit(ImportDeclaration importDeclaration, Object arguments) {
        QualifiedNameExpr nameExpr = (QualifiedNameExpr) importDeclaration.getName();
        importStatements.add(nameExpr.getQualifier().toString() + "." + nameExpr.getName());
    }

    @Nonnull
    public Set<String> getImportStatements() {
        return importStatements;
    }
}
