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

package jatf.common.rules;

import jatf.api.rules.Condition;
import jatf.common.rules.markers.RuleBasedMarker;

import javax.annotation.Nonnull;

@SuppressWarnings("rawtypes")
public interface RuleGenerator<M extends RuleBasedMarker, R extends AnnotationBasedRule<M, ? extends Condition<Class<?>>>> {

  @Nonnull
  ArchitectureTestRule generateArchitectureTestRuleFor(@Nonnull Class<?>[] classes);

  @Nonnull
  R generateRule();
}
