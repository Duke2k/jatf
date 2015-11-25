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

package jatf.common;

import jatf.api.constraints.Constraint;
import jatf.api.constraints.Constraints;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class ArchitectureTestDefaultConstraints extends HashMap<Constraint, String> implements Constraints {

    public ArchitectureTestDefaultConstraints() {
        put(Constraint.INSTABILITY_LOOSE, "0.8");
        put(Constraint.INSTABILITY_STRICT, "0.4");
        put(Constraint.MAX_CHAINED_METHOD_CALLS, "5");
        put(Constraint.MAX_DEPTH_FOR_DFS, "10");
        put(Constraint.MAX_HALSTEAD_DELIVERED_BUGS, "5");
        put(Constraint.MAX_NUMBER_OF_METHODS_PER_CLASS, "20");
        put(Constraint.MAX_NUMBER_OF_STATEMENTS_PER_METHOD, "20");
        put(Constraint.MAXIMUM_CCN, "10");
        put(Constraint.MIN_DEGREE_OF_PURITY, "0.5");
        put(Constraint.ROOT_FOLDER, ".");
        put(Constraint.SCOPES, "jatf");
        put(Constraint.WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER, "false");
    }

    @Override
    @Nonnull
    public String valueOf(@Nonnull Constraint constraint) {
        String result = get(constraint);
        return result == null ? "" : result;
    }
}