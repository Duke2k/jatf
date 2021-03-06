/*
  This file is part of JATF.

  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.

  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.rules;

import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.IArchitectureTest;
import jatf.common.rules.conditions.AlwaysTrue;
import jatf.common.rules.markers.ExcludeMarker;

@SuppressWarnings("unused")
public final class ExcludeRule extends ClassAnnotationBasedRule<ExcludeMarker, AlwaysTrue> {

  public ExcludeRule(Class<?>[] classes, Class<? extends IArchitectureTest>[] tests) {
    super(classes, new AlwaysTrue(), new ExcludeMarker());
    marker.tests = tests;
  }

  public Class<? extends IArchitectureTest>[] tests() {
    return marker.tests;
  }
}
