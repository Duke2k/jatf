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

import jatf.common.parser.DeclarationVisitor;
import jatf.common.parser.MethodVisitor;
import jatf.common.util.ArchitectureTestUtil;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;

@RunWith(DataProviderRunner.class)
public class OrderingTest extends ConventionsTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(OrderingTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testPrivateMethodsAfterProtectedMethodsAfterPublicMethods(Class<?> clazz) {
        int[] methodModifierOrder = {PUBLIC, PROTECTED, PRIVATE};
        MethodVisitor methodVisitor = new MethodVisitor();
        ArchitectureTestUtil.parseWithVoidVisitor(clazz, methodVisitor);
        checkMemberOrder(methodVisitor, methodModifierOrder);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testStaticFieldsBeforePrivateFields(Class<?> clazz) {
        int[] fieldModifierOrder = {STATIC, PRIVATE};
        DeclarationVisitor declarationVisitor = new DeclarationVisitor();
        ArchitectureTestUtil.parseWithVoidVisitor(clazz, declarationVisitor);
        checkMemberOrder(declarationVisitor, fieldModifierOrder);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testConstantsBeforeAnythingElse(Class<?> clazz) {
        int[] fieldModifierOrder = {FINAL, (PUBLIC | PROTECTED | PRIVATE)};
        DeclarationVisitor declarationVisitor = new DeclarationVisitor();
        ArchitectureTestUtil.parseWithVoidVisitor(clazz, declarationVisitor);
        checkMemberOrder(declarationVisitor, fieldModifierOrder);
    }
}
