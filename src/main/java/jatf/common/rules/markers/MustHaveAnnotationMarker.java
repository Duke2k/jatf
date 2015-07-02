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

package jatf.common.rules.markers;

import jatf.annotations.MustHaveAnnotation;

import java.lang.annotation.Annotation;

public class MustHaveAnnotationMarker extends RuleBasedMarker<MustHaveAnnotation> {

    public Class<? extends Annotation> annotation;

    @Override
    public Class<MustHaveAnnotation> annotationType() {
        return MustHaveAnnotation.class;
    }

    @Override
    public MustHaveAnnotation createAnnotation() {
        return new MustHaveAnnotation() {

            @Override
            public String toString() {
                return annotationType().getSimpleName();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MustHaveAnnotation.class;
            }

            @Override
            public Class<? extends Annotation> annotation() {
                return annotation;
            }
        };
    }
}
