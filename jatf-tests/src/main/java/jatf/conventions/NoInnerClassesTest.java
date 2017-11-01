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

import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import jatf.common.parser.DeclarationVisitor;

@RunWith(DataProviderRunner.class)
public class NoInnerClassesTest extends ConventionsTestBase {

	@DataProvider
	public static Object[][] provideClassesToTest() {
		Set<Class<?>> classesToTest = provideClassesFor(NoInnerClassesTest.class);
		return getProvider(classesToTest);
	}

	@Test
	@UseDataProvider(DATA_PROVIDER_NAME)
	public void testForNoInnerClasses(Class<?> clazz) {
		DeclarationVisitor declarationVisitor = new DeclarationVisitor();
		parseWithVoidVisitor(clazz, declarationVisitor);
		assertTrue("Inner class(es) found in " + clazz.getName(), declarationVisitor.getTypeDeclarations().size() < 1);
	}
}
