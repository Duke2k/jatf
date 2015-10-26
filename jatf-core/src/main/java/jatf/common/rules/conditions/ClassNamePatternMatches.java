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

import jatf.api.Condition;

import java.util.regex.Pattern;

public final class ClassNamePatternMatches extends Condition<Class<?>> {

    private Pattern pattern;

    public ClassNamePatternMatches(Class<?> type, String pattern) {
        super(type);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public boolean firesFor(Class<?> type) {
        return pattern.matcher(type.getSimpleName()).matches();
    }
}
