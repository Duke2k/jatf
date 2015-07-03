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

import jatf.pattern.AdapterTest;
import jatf.pattern.SingletonTest;
import jatf.pattern.StateTest;
import jatf.pattern.StrategyTest;
import jatf.pattern.TemplateMethodTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AdapterTest.class,
        SingletonTest.class,
        StateTest.class,
        StrategyTest.class,
        TemplateMethodTest.class
})
public class PatternTests extends ArchitectureTestSuiteBase {

    private static long startTime;

    @BeforeClass
    public static void startUpSuite() {
        startTime = System.currentTimeMillis();
    }

    @AfterClass
    public static void endSuite() {
        endSuite(PatternTests.class, startTime);
    }
}
