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

package jatf.common;

import jatf.api.constraints.Constraint;
import jatf.api.constraints.Constraints;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class ArchitectureTestConstraints {

    public static String[] SCOPES;
    public static String ROOT_FOLDER;
    public static double INSTABILITY_STRICT;
    public static double INSTABILITY_LOOSE;
    public static int MAXIMUM_CCN;
    public static double MAX_HALSTEAD_DELIVERED_BUGS;
    public static int MAX_NUMBER_OF_METHODS_PER_CLASS;
    public static int MAX_NUMBER_OF_STATEMENTS_PER_METHOD;
    public static int MAX_DEPTH_FOR_DFS;
    public static double MIN_DEGREE_OF_PURITY;
    public static int MAX_CHAINED_METHOD_CALLS;
    @SuppressWarnings("WeakerAccess")
    public static boolean WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER;

    static {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        Constraints constraints = (Constraints) context.getBean("constraints");
        SCOPES = constraints.valueOf(Constraint.SCOPES).split(",");
        ROOT_FOLDER = new File(constraints.valueOf(Constraint.ROOT_FOLDER)).getAbsolutePath();
        INSTABILITY_STRICT = Double.parseDouble(constraints.valueOf(Constraint.INSTABILITY_STRICT));
        INSTABILITY_LOOSE = Double.parseDouble(constraints.valueOf(Constraint.INSTABILITY_LOOSE));
        MAXIMUM_CCN = Integer.parseInt(constraints.valueOf(Constraint.MAXIMUM_CCN));
        MAX_HALSTEAD_DELIVERED_BUGS =
                Double.parseDouble(constraints.valueOf(Constraint.MAX_HALSTEAD_DELIVERED_BUGS));
        MAX_NUMBER_OF_METHODS_PER_CLASS =
                Integer.parseInt(constraints.valueOf(Constraint.MAX_NUMBER_OF_METHODS_PER_CLASS));
        MAX_NUMBER_OF_STATEMENTS_PER_METHOD =
                Integer.parseInt(constraints.valueOf(Constraint.MAX_NUMBER_OF_STATEMENTS_PER_METHOD));
        MAX_DEPTH_FOR_DFS = Integer.parseInt(constraints.valueOf(Constraint.MAX_DEPTH_FOR_DFS));
        MIN_DEGREE_OF_PURITY =
                Double.parseDouble(constraints.valueOf(Constraint.MIN_DEGREE_OF_PURITY));
        MAX_CHAINED_METHOD_CALLS =
                Integer.parseInt(constraints.valueOf(Constraint.MAX_CHAINED_METHOD_CALLS));
        WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER =
                Boolean.parseBoolean(constraints.valueOf(Constraint.WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER));
    }
}
