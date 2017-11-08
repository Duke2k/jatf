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

import static com.google.common.collect.Maps.newHashMap;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CastExpressionVisitor extends VoidVisitorAdapter<Object> {

	private Map<Type, Expression> castExpressions = newHashMap();

	@Override
	public void visit(CastExpr castExpr, Object arguments) {
		castExpressions.put(castExpr.getType(), castExpr.getExpression());
	}

	@Nonnull
	public Collection<Expression> getCastExpressions() {
		return castExpressions.values();
	}

	@Nullable
	public Expression getCastExpressionBy(@Nonnull Type type) {
		return castExpressions.get(type);
	}
}
