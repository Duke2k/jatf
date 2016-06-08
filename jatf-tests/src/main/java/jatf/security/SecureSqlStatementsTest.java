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

package jatf.security;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Set;

@RunWith(DataProviderRunner.class)
public class SecureSqlStatementsTest extends SecurityTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(SecureSqlStatementsTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void useDynamicBindingProperly(Class<?> clazz) throws IOException {
        // TODO

    }
}
