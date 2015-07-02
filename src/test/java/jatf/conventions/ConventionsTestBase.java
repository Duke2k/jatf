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

package jatf.conventions;

import jatf.ArchitectureTestBase;
import jatf.common.parser.DeclarationVisitor;
import jatf.common.parser.MethodVisitor;
import japa.parser.ast.visitor.VoidVisitorAdapter;

import java.util.Comparator;
import java.util.List;

import static java.lang.reflect.Modifier.isAbstract;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPrivate;
import static java.lang.reflect.Modifier.isProtected;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static org.junit.Assert.fail;

public abstract class ConventionsTestBase extends ArchitectureTestBase {

    /**
     * This method checks whether the given modifier order in the visitor matches the modifier order given by the array.
     *
     * @param visitor       - the visitor that should be queried
     * @param modifierOrder - the array of modifiers that should be checked for
     */
    protected void checkMemberOrder(VoidVisitorAdapter visitor, int[] modifierOrder) {
        int modifiers[] = new int[0];
        if (visitor instanceof MethodVisitor) {
            List<String> names = ((MethodVisitor) visitor).getMethodNames();
            modifiers = new int[names.size()];
            for (int i = 0; i < names.size(); i++) {
                modifiers[i] = ((MethodVisitor) visitor).getModifierFor(names.get(i));
            }
        } else if (visitor instanceof DeclarationVisitor) {
            List<String> names = ((DeclarationVisitor) visitor).getTypeNames();
            modifiers = new int[names.size()];
            for (int i = 0; i < names.size(); i++) {
                modifiers[i] = ((DeclarationVisitor) visitor).getModifierFor(names.get(i));
            }
        }
        ModifierComparator comparator = new ModifierComparator();
        int currentModifierInOrderIndex = 0;
        for (int modifier : modifiers) {
            for (int j = 0; j < modifierOrder.length; j++) {
                if (comparator.compare(modifier, modifierOrder[j]) == 0) {
                    if (currentModifierInOrderIndex > j) {
                        fail("Modifier ordering conventions violated.");
                    }
                    currentModifierInOrderIndex = j;
                }
            }
        }
    }

    /**
     * This inner class (yes, here we have one, but it is useful here!) is a custom comparator for ordering modifiers.
     */
    protected class ModifierComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer modifiers, Integer singleModifier) {
            if (
                    (isPrivate(modifiers) && isPrivate(singleModifier)) ||
                            (isAbstract(modifiers) && isAbstract(singleModifier)) ||
                            (isPublic(modifiers) && isPublic(singleModifier)) ||
                            (isProtected(modifiers) && isProtected(singleModifier)) ||
                            (isStatic(modifiers) && isStatic(singleModifier)) ||
                            (isFinal(modifiers) && isFinal(singleModifier))
                    ) {
                return 0;
            }
            return modifiers.compareTo(singleModifier);
        }
    }

}
