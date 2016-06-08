package jatf.security;

import com.google.common.io.Files;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import static jatf.common.util.ArchitectureTestUtil.findSourceFileFor;

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
        File sourceFile = findSourceFileFor(clazz);
        if (sourceFile != null) {
            List<String> sourceCodeLines = Files.readLines(sourceFile, Charset.defaultCharset());

        }
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void noClearTextCredentialsInPreparedStatement(Class<?> clazz) throws IOException {
        File sourceFile = findSourceFileFor(clazz);
        if (sourceFile != null) {
            List<String> sourceCodeLines = Files.readLines(sourceFile, Charset.defaultCharset());

        }
    }
}
