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

package jatf.dependency;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.parser.AssignmentVisitor;
import jatf.common.parser.CastExpressionVisitor;
import jatf.common.parser.DeclarationVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertNull;

@RunWith(DataProviderRunner.class)
public class DangerousCastsTest extends DependencyTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(DangerousCastsTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForNarrowingPrimitiveTypeCastToInt(Class<?> clazz) {
    Set<PrimitiveType> dangerousSourceTypes = newHashSet();
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.DOUBLE));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.LONG));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.FLOAT));
    findDangerousCasts(clazz, new PrimitiveType(PrimitiveType.Primitive.INT), dangerousSourceTypes);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForNarrowingPrimitiveTypeCastToFloat(Class<?> clazz) {
    Set<PrimitiveType> dangerousSourceTypes = newHashSet();
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.DOUBLE));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.LONG));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.INT));
    findDangerousCasts(clazz, new PrimitiveType(PrimitiveType.Primitive.FLOAT), dangerousSourceTypes);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForNarrowingPrimitiveTypeCastToShort(Class<?> clazz) {
    Set<PrimitiveType> dangerousSourceTypes = newHashSet();
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.DOUBLE));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.LONG));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.FLOAT));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.INT));
    findDangerousCasts(clazz, new PrimitiveType(PrimitiveType.Primitive.SHORT), dangerousSourceTypes);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForNarrowingPrimitiveTypeCastToLong(Class<?> clazz) {
    Set<PrimitiveType> dangerousSourceTypes = newHashSet();
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.DOUBLE));
    dangerousSourceTypes.add(new PrimitiveType(PrimitiveType.Primitive.FLOAT));
    findDangerousCasts(clazz, new PrimitiveType(PrimitiveType.Primitive.LONG), dangerousSourceTypes);
  }

  private void findDangerousCasts(@Nonnull Class<?> clazz, @Nonnull PrimitiveType targetType, @Nonnull Set<PrimitiveType> dangerousSourceTypes) {
    AssignmentVisitor assignmentVisitor = new AssignmentVisitor();
    parseWithVoidVisitor(clazz, assignmentVisitor);
    DeclarationVisitor declarationVisitor = new DeclarationVisitor();
    parseWithVoidVisitor(clazz, declarationVisitor);
    Set<AssignExpr> assignments = assignmentVisitor.getAssignExprs();
    for (AssignExpr assignment : assignments) {
      String targetName = assignment.getTarget().toString();
      Type type = declarationVisitor.getTypeBy(targetName);
      if (type instanceof PrimitiveType && ((PrimitiveType) type).getType().equals(targetType.getType())) {
        CastExpressionVisitor castExpressionVisitor = new CastExpressionVisitor();
        parseWithVoidVisitor(assignment, castExpressionVisitor);
        for (Type sourceType : dangerousSourceTypes) {
          Expression expression = castExpressionVisitor.getCastExpressionBy(sourceType);
          assertNull("Dangerous cast operation found: " + expression + " in " + assignment + "within " + clazz.getName(), expression);
        }
      }
    }
  }
}
