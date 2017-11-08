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

package jatf.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import jatf.conventions.CamelCaseTest;
import jatf.conventions.NoFinalModifierInLocalVariablesTest;
import jatf.conventions.NoInnerClassesTest;
import jatf.conventions.OrderingTest;
import jatf.conventions.OverlyChainedMethodCallsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		CamelCaseTest.class,
		NoInnerClassesTest.class,
		OrderingTest.class,
		OverlyChainedMethodCallsTest.class,
		NoFinalModifierInLocalVariablesTest.class
})
public class ConventionTests extends ArchitectureTestSuiteBase {

	private static long startTime;

	@BeforeClass
	public static void startUpSuite() {
		startTime = System.currentTimeMillis();
	}

	@AfterClass
	public static void endSuite() {
		endSuite(ConventionTests.class, startTime);
	}
}
