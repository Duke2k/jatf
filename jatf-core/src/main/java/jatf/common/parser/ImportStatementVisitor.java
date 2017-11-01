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


import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import javax.annotation.Nonnull;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ImportStatementVisitor extends VoidVisitorAdapter<Object> {

	private Set<String> importStatements = newHashSet();

	@Override
	public void visit(ImportDeclaration importDeclaration, Object arguments) {
		if (importDeclaration.getName().getQualifier().isPresent()) {
			importStatements.add(importDeclaration.getName().getQualifier().get().asString());
		}
	}

	@Nonnull
	public Set<String> getImportStatements() {
		return importStatements;
	}
}
