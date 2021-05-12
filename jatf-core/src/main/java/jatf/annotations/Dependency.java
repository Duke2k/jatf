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

package jatf.annotations;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public enum Dependency {
  NONE,
  EXTENDS,
  IMPLEMENTS,
  THROWS,
  USES,
  OVERRIDES,
  RETURNS,
  ADP,
  DANGEROUS_CASTS,
  INSTABILITY,
  UNCHECKED_CASTS,
  METHOD_PURITY,
  ANNOTATION_TYPE,
  ALL;

  /**
   * This method adds a dependency in a way that it checks if, by the addition, the set would be complete. If that
   * is the case, the complete set is replaced by Dependency.all. It also avoids duplicate entries.
   *
   * @param orig  - the original array of Dependencies
   * @param toAdd - the Dependency to be added
   * @return the new array of Dependencies
   */
  @SuppressWarnings("unused")
  @Nonnull
  public static Dependency[] add(Dependency[] orig, Dependency toAdd) {
    Set<Dependency> origSet = newHashSet();
    if (origSet.contains(ALL) && origSet.size() == 1) {
      return orig;
    }
    if (origSet.contains(NONE) && origSet.size() == 1) {
      return new Dependency[]{toAdd};
    }
    origSet.add(toAdd);
    if (origSet.size() == Dependency.values().length - 2) {
      return new Dependency[]{ALL};
    }
    return (Dependency[]) origSet.toArray();
  }

  /**
   * This method removes a dependency in a way that it checks if it is removed from Dependency.all. If that is the
   * case, an array of all values is constructed, then Dependency.all, Dependency.none and the dependency to be
   * removed are removed.
   *
   * @param orig     - the original array of Dependencies
   * @param toRemove - the Dependency to be removed
   * @return the new array of Dependencies
   */
  @SuppressWarnings("unused")
  @Nonnull
  public static Dependency[] remove(Dependency[] orig, Dependency toRemove) {
    Set<Dependency> origSet = newHashSet();
    if (origSet.contains(NONE) && origSet.size() == 1) {
      return orig;
    }
    if (origSet.contains(toRemove) && origSet.size() == 1) {
      return new Dependency[]{NONE};
    }
    if (origSet.contains(ALL) && origSet.size() == 1) {
      origSet.addAll(Arrays.asList(Dependency.values()));
      origSet.remove(ALL);
      origSet.remove(NONE);
    }
    origSet.remove(toRemove);
    return (Dependency[]) origSet.toArray();
  }
}
