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

package jatf.api.constraints;

public enum Constraint {
    SCOPES,
    ROOT_FOLDER,
    INSTABILITY_STRICT,
    INSTABILITY_LOOSE,
    MAXIMUM_CCN,
    MAX_HALSTEAD_DELIVERED_BUGS,
    MAX_NUMBER_OF_METHODS_PER_CLASS,
    MAX_NUMBER_OF_STATEMENTS_PER_METHOD,
    MAX_DEPTH_FOR_DFS,
    MIN_DEGREE_OF_PURITY,
    MAX_CHAINED_METHOD_CALLS,
    WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER
}
