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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static jatf.common.ArchitectureTestRunListener.report;

public class ArchitectureTestConstants {

    private static Properties properties;
    static {
        properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("jatf.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            report("Could not load jatf.properties:", e);
        }
    }
    public static final String[] SCOPES = properties.getProperty("scopes").split(",");

    public static final String ROOT_FOLDER = properties.getProperty("root.folder");

    public static final double INSTABILITY_STRICT = Double.parseDouble(properties.getProperty("instability.strict"));
    public static final double INSTABILITY_LOOSE = Double.parseDouble(properties.getProperty("instability.loose"));
    public static final int MAXIMUM_CCN = Integer.parseInt(properties.getProperty("cyclomatic-complexity.maximum"));
    public static final double MAX_HALSTEAD_DELIVERED_BUGS =
            Double.parseDouble(properties.getProperty("halstead-delivered-bugs.maximum"));
    public static final int MAX_NUMBER_OF_METHODS_PER_CLASS =
            Integer.parseInt(properties.getProperty("methods-per-class.maximum"));
    public static final int MAX_NUMBER_OF_STATEMENTS_PER_METHOD =
            Integer.parseInt(properties.getProperty("statements-per-method.maximum"));
    public static final int MAX_DEPTH_FOR_DFS = Integer.parseInt(properties.getProperty("depth-for-dfs.maximum"));
    public static final double MIN_DEGREE_OF_PURITY =
            Double.parseDouble(properties.getProperty("degree-of-purity.minimum"));
    public static final int MAX_CHAINED_METHOD_CALLS =
            Integer.parseInt(properties.getProperty("chained-method-calls.maximum"));
}
