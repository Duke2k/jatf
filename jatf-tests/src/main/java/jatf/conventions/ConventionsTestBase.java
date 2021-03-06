/*
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

package jatf.conventions;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import jatf.ArchitectureTestBase;
import jatf.common.parser.DeclarationVisitor;
import jatf.common.parser.MethodVisitor;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("WeakerAccess")
public abstract class ConventionsTestBase extends ArchitectureTestBase {

  /**
   * This method checks whether the given modifier order in the visitor matches the modifier order given by the array.
   *
   * @param visitor       - the visitor that should be queried
   * @param modifierOrder - the array of modifiers that should be checked for
   */
  protected <A> void checkMemberOrder(VoidVisitorAdapter<A> visitor, List<Modifier> modifierOrder) {
    List<Modifier> modifiers = newArrayList();
    if (visitor instanceof MethodVisitor) {
      List<String> names = ((MethodVisitor) visitor).getMethodNames();
      for (String name : names) {
        modifiers.addAll(((MethodVisitor) visitor).getModifierFor(name));
      }
    } else if (visitor instanceof DeclarationVisitor) {
      List<String> names = ((DeclarationVisitor) visitor).getTypeNames();
      for (String name : names) {
        modifiers.addAll(((DeclarationVisitor) visitor).getModifierFor(name));
      }
    }
    assertSameOrder(modifiers, modifierOrder);
  }

  private void assertSameOrder(List<Modifier> modifiers, List<Modifier> modifierOrder) {
    int modifierOrderPointer = 0;
    for (Modifier m : modifiers) {
      int currentOrderIndex = 0;
      for (Modifier mo : modifierOrder) {
        if (!mo.getKeyword().equals(m.getKeyword()) && currentOrderIndex++ > modifierOrderPointer) {
          modifierOrderPointer++;
        }
      }
    }
    assertTrue(modifierOrderPointer <= modifierOrder.size());
  }
}
