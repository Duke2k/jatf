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

package jatf.common.rules.markers;

import jatf.annotations.MustNotImplement;

import java.lang.annotation.Annotation;

public class MustNotImplementMarker extends RuleBasedMarker<MustNotImplement> {

  public Class<?>[] interfaces = new Class<?>[0];

  @Override
  public Class<MustNotImplement> annotationType() {
    return MustNotImplement.class;
  }

  @Override
  public MustNotImplement createAnnotation() {
    return new MustNotImplement() {

      @Override
      public String toString() {
        return annotationType().getSimpleName();
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return MustNotImplement.class;
      }

      @Override
      public Class<?>[] interfaces() {
        return interfaces;
      }
    };
  }
}
