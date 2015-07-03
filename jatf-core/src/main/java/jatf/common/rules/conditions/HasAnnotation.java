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

package jatf.common.rules.conditions;

import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;

public final class HasAnnotation extends Condition<Class<?>> {

    public HasAnnotation(@Nonnull Annotation annotation) {
        super(annotation.annotationType());
    }

    public HasAnnotation(@Nonnull Class<? extends Annotation> annotationType) {
        super(annotationType);
    }

    @Override
    public boolean firesFor(Class<?> type) {
        Annotation[] annotations = type.getAnnotations();
        if (annotations != null) {
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(this.type)) {
                    return true;
                }
            }
        }
        return false;
    }
}
