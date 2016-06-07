package jatf.security;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;

import static jatf.common.ArchitectureTestRunListener.report;

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
