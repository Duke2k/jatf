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

import jatf.annotations.MustThrow;

public class MustThrowMarker extends RuleBasedMarker<MustThrow> {

	@SuppressWarnings("UnusedDeclaration")
	public Class<? extends Throwable>[] throwables;

	@Override
	public Class<MustThrow> annotationType() {
		return MustThrow.class;
	}

	@Override
	public MustThrow createAnnotation() {
		return new MustThrow() {

			@Override
			public String toString() {
				return annotationType().getSimpleName();
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return MustThrow.class;
			}

			@Override
			public Class<? extends Throwable>[] throwables() {
				return throwables;
			}
		};
	}
}
