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

package jatf.common.rules;

import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.rules.conditions.Implements;
import jatf.common.rules.markers.MustOverrideMarker;

@SuppressWarnings("unused")
public final class OverrideIfImplementsRule extends ClassAnnotationBasedRule<MustOverrideMarker, Implements> {

  public OverrideIfImplementsRule(Class<?> toBeImplemented, Class<?>[] classes, MustOverrideMarker marker) {
    super(classes, new Implements(toBeImplemented), marker);
  }

  public String[] methodNames() {
    return marker.methodNames;
  }
}
