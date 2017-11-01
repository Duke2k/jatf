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

package jatf.common.rules;

import java.lang.reflect.Method;

import jatf.api.rules.MethodAnnotationBasedRule;
import jatf.common.rules.conditions.AlwaysTrue;
import jatf.common.rules.markers.MustReturnMarker;

@SuppressWarnings("unused")
public final class MustReturnRule extends MethodAnnotationBasedRule<MustReturnMarker, AlwaysTrue> {

	public MustReturnRule(Method[] methods, MustReturnMarker marker) {
		super(methods, new AlwaysTrue(), marker);
	}

	public Class<?>[] types() {
		return marker.types;
	}
}
