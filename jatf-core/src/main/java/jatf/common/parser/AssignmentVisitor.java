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

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class AssignmentVisitor extends VoidVisitorAdapter<Object> {

	private Set<Assignment> assignments = newHashSet();
	private Set<AssignExpr> assignExprs = newHashSet();

	@Override
	public void visit(AssignExpr assignExpr, Object arguments) {
		assignExprs.add(assignExpr);
		assignments.add(new Assignment(
				assignExpr.getTarget(),
				assignExpr.getOperator(),
				assignExpr.getValue()
		));
	}

	public Set<Assignment> getAssignments() {
		return assignments;
	}

	public Set<AssignExpr> getAssignExprs() {
		return assignExprs;
	}

	public class Assignment {

		private Expression target;
		private AssignExpr.Operator operator;
		private Expression value;

		Assignment(Expression target, AssignExpr.Operator operator, Expression value) {
			this.target = target;
			this.operator = operator;
			this.value = value;
		}

		public Expression getTarget() {
			return target;
		}

		@SuppressWarnings("unused")
		public AssignExpr.Operator getOperator() {
			return operator;
		}

		@SuppressWarnings("unused")
		public Expression getValue() {
			return value;
		}
	}
}
