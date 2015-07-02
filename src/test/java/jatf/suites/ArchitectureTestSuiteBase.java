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

import jatf.common.ArchitectureTestRunListener;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public abstract class ArchitectureTestSuiteBase {

    protected static void endSuite(@Nonnull Class<? extends ArchitectureTestSuiteBase> suiteClass, @Nonnegative long startTime) {
        long endTime = System.currentTimeMillis();
        ArchitectureTestRunListener.report(suiteClass.getSimpleName() + " finished in " + (endTime - startTime) + " ms.");
    }
}
