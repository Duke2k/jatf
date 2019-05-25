/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common.util;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

public class LogUtil {

  @Nonnull
  public static Logger getLogger(@Nonnull Class<?> clazz) {
    return Logger.getLogger(clazz.getName());
  }

  @Nonnull
  public static Logger getLogger() {
    return Logger.getGlobal();
  }
}
