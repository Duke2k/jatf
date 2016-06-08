package jatf.suites;


import jatf.security.FileUserRightsTest;
import jatf.security.SecureSqlStatementsTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        FileUserRightsTest.class,
        SecureSqlStatementsTest.class
})
public class SecurityTests extends ArchitectureTestSuiteBase {

    private static long startTime;

    @BeforeClass
    public static void startUpSuite() {
        startTime = System.currentTimeMillis();
    }

    @AfterClass
    public static void endSuite() {
        endSuite(SecurityTests.class, startTime);
    }
}
