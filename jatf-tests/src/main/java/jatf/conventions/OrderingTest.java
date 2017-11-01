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

package jatf.conventions;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.javaparser.ast.Modifier;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import jatf.common.parser.DeclarationVisitor;
import jatf.common.parser.MethodVisitor;
import jatf.common.util.ArchitectureTestUtil;

@RunWith(DataProviderRunner.class)
public class OrderingTest extends ConventionsTestBase {

	@DataProvider
	public static Object[][] provideClassesToTest() {
		Set<Class<?>> classesToTest = provideClassesFor(OrderingTest.class);
		return getProvider(classesToTest);
	}

	@Test
	@UseDataProvider(DATA_PROVIDER_NAME)
	public void privateMethodsAfterProtectedMethodsAfterPublicMethods(Class<?> clazz) {
		List<EnumSet<Modifier>> methodModifierOrder = Arrays.asList(
				EnumSet.of(Modifier.PUBLIC),
				EnumSet.of(Modifier.DEFAULT),
				EnumSet.of(Modifier.PROTECTED),
				EnumSet.of(Modifier.PRIVATE)
		);
		MethodVisitor methodVisitor = new MethodVisitor();
		ArchitectureTestUtil.parseWithVoidVisitor(clazz, methodVisitor);
		checkMemberOrder(methodVisitor, methodModifierOrder);
	}

	@Test
	@UseDataProvider(DATA_PROVIDER_NAME)
	public void staticFieldsBeforePrivateFields(Class<?> clazz) {
		List<EnumSet<Modifier>> fieldModifierOrder = Arrays.asList(
				EnumSet.of(Modifier.STATIC),
				EnumSet.of(Modifier.PRIVATE)
		);
		DeclarationVisitor declarationVisitor = new DeclarationVisitor();
		ArchitectureTestUtil.parseWithVoidVisitor(clazz, declarationVisitor);
		checkMemberOrder(declarationVisitor, fieldModifierOrder);
	}

	@Test
	@UseDataProvider(DATA_PROVIDER_NAME)
	public void constantsBeforeAnythingElse(Class<?> clazz) {
		List<EnumSet<Modifier>> fieldModifierOrder = Arrays.asList(
				EnumSet.of(Modifier.FINAL),
				EnumSet.of(Modifier.PUBLIC, Modifier.DEFAULT, Modifier.PROTECTED, Modifier.PRIVATE)
		);
		DeclarationVisitor declarationVisitor = new DeclarationVisitor();
		ArchitectureTestUtil.parseWithVoidVisitor(clazz, declarationVisitor);
		checkMemberOrder(declarationVisitor, fieldModifierOrder);
	}
}
