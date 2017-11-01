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

import java.lang.annotation.Annotation;

import jatf.annotations.MustNotUse;

public class MustNotUseMarker extends RuleBasedMarker<MustNotUse> {

	public Class<?>[] types = new Class<?>[0];

	@Override
	public Class<MustNotUse> annotationType() {
		return MustNotUse.class;
	}

	@Override
	public MustNotUse createAnnotation() {
		return new MustNotUse() {

			@Override
			public String toString() {
				return annotationType().getSimpleName();
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return MustNotUse.class;
			}

			@Override
			public Class<?>[] types() {
				return types;
			}
		};
	}
}
