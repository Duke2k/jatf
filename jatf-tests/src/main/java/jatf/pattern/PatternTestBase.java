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

package jatf.pattern;

import jatf.ArchitectureTestBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Member;

public abstract class PatternTestBase extends ArchitectureTestBase {

  /**
   * This generic method finds a member in the given array by its name, or null if it doesn't exist. It is used in
   * several pattern tests.
   *
   * @param name    - name of the member to be found
   * @param members - array of members to be searched in
   * @return the member found by its name, or null if it doesn't exist
   */
  @Nullable
  protected <M extends Member> M findMemberBy(@Nonnull String name, @Nonnull M[] members) {
    M result = null;
    for (M member : members) {
      if (member.getName().equals(name)) {
        result = member;
        break;
      }
    }
    return result;
  }
}
