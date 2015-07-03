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

import jatf.annotations.MustImplement;

import java.lang.annotation.Annotation;

public class MustImplementMarker extends RuleBasedMarker<MustImplement> {

    public Class<?>[] interfaces = new Class<?>[0];

    @Override
    public Class<MustImplement> annotationType() {
        return MustImplement.class;
    }

    @Override
    public MustImplement createAnnotation() {
        return new MustImplement() {

            @Override
            public String toString() {
                return annotationType().getSimpleName();
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return MustImplement.class;
            }

            @Override
            public Class<?>[] interfaces() {
                return interfaces;
            }
        };
    }
}
