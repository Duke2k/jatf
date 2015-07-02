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

import japa.parser.ast.expr.CastExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.type.Type;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

public class CastExpressionVisitor extends VoidVisitorAdapter<Object> {

    private Map<Type, Expression> castExpressions = newHashMap();

    @Override
    public void visit(CastExpr castExpr, Object arguments) {
        castExpressions.put(castExpr.getType(), castExpr.getExpr());
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
