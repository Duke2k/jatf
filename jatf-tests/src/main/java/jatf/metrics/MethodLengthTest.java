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

package jatf.metrics;

import static jatf.common.ArchitectureTestConstraints.MAX_NUMBER_OF_STATEMENTS_PER_METHOD;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.javaparser.ast.stmt.Statement;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import jatf.common.parser.MethodVisitor;

@RunWith(DataProviderRunner.class)
public class MethodLengthTest extends MetricsTestBase {

	@DataProvider
	public static Object[][] provideClassesToTest() {
		Set<Class<?>> classesToTest = provideClassesFor(MethodLengthTest.class);
		return getProvider(classesToTest);
	}

	@Test
	@UseDataProvider(DATA_PROVIDER_NAME)
	public void testMethodLengths(Class<?> clazz) {
		MethodVisitor methodVisitor = new MethodVisitor();
		parseWithVoidVisitor(clazz, methodVisitor);
		for (String methodName : methodVisitor.getMethodNames()) {
			List<Statement> statements = methodVisitor.getStatementsInMethod(methodName);
			if (statements != null) {
				assertTrue("Method length threshold violated in " + methodName + " of " + clazz.getName(),
						statements.size() <= MAX_NUMBER_OF_STATEMENTS_PER_METHOD);
			}
		}
	}
}
