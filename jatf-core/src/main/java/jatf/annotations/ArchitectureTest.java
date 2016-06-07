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

package jatf.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static jatf.annotations.Dependency.none;
import static jatf.annotations.Pattern.undefined;

@Retention(RetentionPolicy.RUNTIME)
public @interface ArchitectureTest {

    boolean omitMetrics() default false;

    boolean omitConventions() default false;

    Pattern[] patterns() default {undefined};

    Dependency[] dependencies() default {none};

    boolean enforceSecurityTests() default true;

    String[] testNames() default {};
}
