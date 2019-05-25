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

import jatf.dependency.AcyclicDependenciesPrincipleTest;
import jatf.dependency.AnnotationTypeTest;
import jatf.dependency.DangerousCastsTest;
import jatf.dependency.ExtendsTest;
import jatf.dependency.ImplementsTest;
import jatf.dependency.InstabilityTest;
import jatf.dependency.MethodPurityTest;
import jatf.dependency.OverridesTest;
import jatf.dependency.ReturnsTest;
import jatf.dependency.ThrowsTest;
import jatf.dependency.UncheckedCastsTest;
import jatf.dependency.UsesTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    AcyclicDependenciesPrincipleTest.class,
    AnnotationTypeTest.class,
    DangerousCastsTest.class,
    ExtendsTest.class,
    ImplementsTest.class,
    InstabilityTest.class,
    MethodPurityTest.class,
    OverridesTest.class,
    ThrowsTest.class,
    UncheckedCastsTest.class,
    UsesTest.class,
    ReturnsTest.class
})
public class DependencyTests extends ArchitectureTestSuiteBase {

  private static long startTime;

  @BeforeClass
  public static void startUpSuite() {
    startTime = System.currentTimeMillis();
  }

  @AfterClass
  public static void endSuite() {
    endSuite(DependencyTests.class, startTime);
  }
}
