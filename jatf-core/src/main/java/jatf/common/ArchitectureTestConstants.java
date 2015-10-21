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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Properties;

import static jatf.common.ArchitectureTestRunListener.report;

public class ArchitectureTestConstants {

    public static String[] SCOPES = "".split(",");
    public static String ROOT_FOLDER = ".";
    public static double INSTABILITY_STRICT = 0.4;
    public static double INSTABILITY_LOOSE = 0.6;
    public static int MAXIMUM_CCN = 10;
    public static double MAX_HALSTEAD_DELIVERED_BUGS = 3;
    public static int MAX_NUMBER_OF_METHODS_PER_CLASS = 20;
    public static int MAX_NUMBER_OF_STATEMENTS_PER_METHOD = 20;
    public static int MAX_DEPTH_FOR_DFS = 10;
    public static double MIN_DEGREE_OF_PURITY = 0.5;
    public static int MAX_CHAINED_METHOD_CALLS = 5;
    public static ArchitectureTestConstantsChangeNotifier notifier = new ArchitectureTestConstantsChangeNotifier();

    private static Properties properties;
    private static ArchitectureTestConfiguration configuration;

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

    static {
        try {
            SCOPES = properties.getProperty("scopes").split(",");
            ROOT_FOLDER = properties.getProperty("root.folder");
            INSTABILITY_STRICT = Double.parseDouble(properties.getProperty("instability.strict"));
            INSTABILITY_LOOSE = Double.parseDouble(properties.getProperty("instability.loose"));
            MAXIMUM_CCN = Integer.parseInt(properties.getProperty("cyclomatic-complexity.maximum"));
            MAX_HALSTEAD_DELIVERED_BUGS =
                    Double.parseDouble(properties.getProperty("halstead-delivered-bugs.maximum"));
            MAX_NUMBER_OF_METHODS_PER_CLASS =
                    Integer.parseInt(properties.getProperty("methods-per-class.maximum"));
            MAX_NUMBER_OF_STATEMENTS_PER_METHOD =
                    Integer.parseInt(properties.getProperty("statements-per-method.maximum"));
            MAX_DEPTH_FOR_DFS = Integer.parseInt(properties.getProperty("depth-for-dfs.maximum"));
            MIN_DEGREE_OF_PURITY =
                    Double.parseDouble(properties.getProperty("degree-of-purity.minimum"));
            MAX_CHAINED_METHOD_CALLS =
                    Integer.parseInt(properties.getProperty("chained-method-calls.maximum"));
        } catch (Exception e) {
            ArchitectureTestRunListener.report("Unable to query properties, using default constraints instead.", e);
        }
    }

    @SuppressWarnings("unused")
    public static void initWith(@Nonnull ArchitectureTestConfiguration conf) {
        setConfiguration(conf);
        init();
    }

    public static void setConfiguration(@Nonnull ArchitectureTestConfiguration conf) {
        configuration = conf;
    }

    public static void init() {
        if (configuration != null) {
            SCOPES = configuration.getProperty("scopes").split(",");
            ROOT_FOLDER = configuration.getProperty("root.folder");
            INSTABILITY_STRICT = Double.parseDouble(configuration.getProperty("instability.strict"));
            INSTABILITY_LOOSE = Double.parseDouble(configuration.getProperty("instability.loose"));
            MAXIMUM_CCN = Integer.parseInt(configuration.getProperty("cyclomatic-complexity.maximum"));
            MAX_HALSTEAD_DELIVERED_BUGS =
                    Double.parseDouble(configuration.getProperty("halstead-delivered-bugs.maximum"));
            MAX_NUMBER_OF_METHODS_PER_CLASS =
                    Integer.parseInt(configuration.getProperty("methods-per-class.maximum"));
            MAX_NUMBER_OF_STATEMENTS_PER_METHOD =
                    Integer.parseInt(configuration.getProperty("statements-per-method.maximum"));
            MAX_DEPTH_FOR_DFS = Integer.parseInt(configuration.getProperty("depth-for-dfs.maximum"));
            MIN_DEGREE_OF_PURITY =
                    Double.parseDouble(configuration.getProperty("degree-of-purity.minimum"));
            MAX_CHAINED_METHOD_CALLS =
                    Integer.parseInt(configuration.getProperty("chained-method-calls.maximum"));
            notifier.doIt();
        }
    }

    public static class ArchitectureTestConstantsChangeNotifier extends Observable {

        public void doIt() {
            setChanged();
            notifyObservers();
        }
    }
}
