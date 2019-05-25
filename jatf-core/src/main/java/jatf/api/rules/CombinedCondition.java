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

package jatf.api.rules;

import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public abstract class CombinedCondition<T> extends Condition<T> {

  protected Set<Condition<Class<?>>> conditions;

  @SafeVarargs
  public CombinedCondition(T type, Condition<Class<?>>... conditions) {
    super(type);
    this.conditions = newHashSet();
    this.conditions.addAll(Arrays.asList(conditions));
  }
}
