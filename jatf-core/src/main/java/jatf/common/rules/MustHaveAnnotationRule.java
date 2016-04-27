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

package jatf.common.rules;

import jatf.api.rules.ClassAnnotationBasedRule;
import jatf.common.rules.conditions.AlwaysTrue;
import jatf.common.rules.markers.MustHaveAnnotationMarker;

import java.lang.annotation.Annotation;

@SuppressWarnings("unused")
public final class MustHaveAnnotationRule extends ClassAnnotationBasedRule<MustHaveAnnotationMarker, AlwaysTrue> {

    public MustHaveAnnotationRule(Class<?>[] classes, MustHaveAnnotationMarker marker) {
        super(classes, new AlwaysTrue(), marker);
    }

    public Class<? extends Annotation> annotation() {
        return marker.annotation;
    }
}
