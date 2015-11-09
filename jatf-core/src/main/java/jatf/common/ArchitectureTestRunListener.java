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

import jatf.annotations.ArchitectureTest;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArchitectureTestRunListener extends RunListener {

    public static final String PREFIX = "";

    private static Logger staticLog = LoggerFactory.getLogger(ArchitectureTest.class);

    public static void report(String message, Object... items) {
        StringBuilder stringBuilder = new StringBuilder(PREFIX);
        stringBuilder.append(message);
        for (Object item : items) {
            stringBuilder.append(System.getProperty("line.separator"));
            stringBuilder.append(PREFIX);
            stringBuilder.append(item);
        }
        staticLog.info(stringBuilder.toString());
    }
}
