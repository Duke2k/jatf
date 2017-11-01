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

import java.util.logging.Logger;

import org.junit.runner.notification.RunListener;

import jatf.common.util.LogUtil;

public class ArchitectureTestRunListener extends RunListener {

	@SuppressWarnings("WeakerAccess")
	public static final String PREFIX = "";

	private static Logger log = LogUtil.getLogger(ArchitectureTestRunListener.class);

	public static void report(String message, Object... items) {
		StringBuilder stringBuilder = new StringBuilder(PREFIX);
		stringBuilder.append(message);
		for (Object item : items) {
			stringBuilder.append(System.getProperty("line.separator"));
			stringBuilder.append(PREFIX);
			stringBuilder.append(item);
		}
		log.info(stringBuilder.toString());
	}
}
