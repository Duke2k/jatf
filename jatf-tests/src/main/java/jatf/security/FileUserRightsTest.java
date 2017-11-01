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

import static jatf.common.ArchitectureTestRunListener.report;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

@RunWith(DataProviderRunner.class)
public class FileUserRightsTest extends SecurityTestBase {

	@DataProvider
	public static Object[][] provideClassesToTest() {
		Set<Class<?>> classesToTest = provideClassesFor(FileUserRightsTest.class);
		return getProvider(classesToTest);
	}

	@Test
	@UseDataProvider(DATA_PROVIDER_NAME)
	public void configFilesAreNotExecutable(Class<?> clazz) {
		Enumeration<URL> resources = null;
		try {
			resources = clazz.getClassLoader().getResources("*");
		} catch (IOException e) {
			report("Resources for " + clazz + " could not be loaded.", e);
		}
		if (resources != null) {
			while (resources.hasMoreElements()) {
				URL resource = resources.nextElement();
				File resourceFile = new File(resource.getFile());
				Assert.assertFalse("Resource " + resource + " must not be executable!", resourceFile.canExecute());
			}
		}
	}
}
