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

import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.expr.AssignExpr.Operator;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

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
        private Operator operator;
        private Expression value;

        Assignment(Expression target, Operator operator, Expression value) {
            this.target = target;
            this.operator = operator;
            this.value = value;
        }

        public Expression getTarget() {
            return target;
        }

        public Operator getOperator() {
            return operator;
        }

        public Expression getValue() {
            return value;
        }
    }
}
