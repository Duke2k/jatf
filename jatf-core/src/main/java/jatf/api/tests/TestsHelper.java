/*
  This file is part of JATF.
  <p/>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p/>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p/>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.api.tests;

import jatf.api.constraints.Constraints;
import jatf.common.ArchitectureTestDataProvider;
import jatf.common.ArchitectureTestDefaultConstraints;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

import static jatf.common.util.ArchitectureTestUtil.resetReflections;
import static jatf.common.util.ArchitectureTestUtil.resetSourceFilesMap;

@SuppressWarnings("unused")
public class TestsHelper {

    private static ArchitectureTestDataProvider dataProvider;

    public static void resetCodebaseForTests() {
        resetReflections();
        resetSourceFilesMap();
    }

    @Nullable
    public static Set<Class<?>> getClassesForTest(@Nonnull String testName) {
        if (dataProvider == null) {
            dataProvider = new ArchitectureTestDataProvider();
        }
        return dataProvider.getClassesFor(testName);
    }

    @Nonnull
    public static Class<? extends Constraints> getDefaultConstraintsType() {
        return ArchitectureTestDefaultConstraints.class;
    }

    @Nonnull
    public static ArchitectureTestDefaultConstraints getDefaultConstraints() {
        return new ArchitectureTestDefaultConstraints();
    }
}
