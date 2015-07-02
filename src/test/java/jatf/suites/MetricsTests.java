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

package jatf.suites;

import jatf.metrics.CyclomaticComplexityTest;
import jatf.metrics.HalsteadComplexityTest;
import jatf.metrics.MethodLengthTest;
import jatf.metrics.NumberOfMethodsPerClassTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CyclomaticComplexityTest.class,
        HalsteadComplexityTest.class,
        MethodLengthTest.class,
        NumberOfMethodsPerClassTest.class
})
public class MetricsTests extends ArchitectureTestSuiteBase {

    private static long startTime;

    @BeforeClass
    public static void startUpSuite() {
        startTime = System.currentTimeMillis();
    }

    @AfterClass
    public static void endSuite() {
        endSuite(MetricsTests.class, startTime);
    }
}
