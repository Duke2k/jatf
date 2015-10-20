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

package jatf.conventions;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.parser.DeclarationVisitor;
import jatf.common.parser.MethodVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;
import java.util.regex.Pattern;

import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class CamelCaseTest extends ConventionsTestBase {

    public static final Pattern CLASS_CAMEL_CASE_PATTERN =
            Pattern.compile(properties.getProperty("namePattern.for.classes"));
    public static final Pattern METHOD_CAMEL_CASE_PATTERN =
            Pattern.compile(properties.getProperty("namePattern.for.methods"));
    public static final Pattern FIELD_CAMEL_CASE_PATTERN =
            Pattern.compile(properties.getProperty("namePattern.for.fields"));
    public static final Pattern CONST_CAMEL_CASE_PATTERN =
            Pattern.compile(properties.getProperty("namePattern.for.constants"));

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(CamelCaseTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testClassNaming(Class<?> clazz) {
        assertTrue("Class " + clazz.getSimpleName() + "(" + clazz.getName() + ") does not match " + CLASS_CAMEL_CASE_PATTERN,
                CLASS_CAMEL_CASE_PATTERN.matcher(clazz.getSimpleName()).matches());
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testMethodNaming(Class<?> clazz) {
        MethodVisitor methodVisitor = new MethodVisitor();
        parseWithVoidVisitor(clazz, methodVisitor);
        for (String methodName : methodVisitor.getMethodNames()) {
            assertTrue("Method " + methodName + " in class " + clazz.getName() + " does not match " + METHOD_CAMEL_CASE_PATTERN,
                    METHOD_CAMEL_CASE_PATTERN.matcher(methodName).matches());
        }
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testFieldNaming(Class<?> clazz) {
        DeclarationVisitor declarationVisitor = new DeclarationVisitor();
        parseWithVoidVisitor(clazz, declarationVisitor);
        for (String fieldName : declarationVisitor.getTypeNames()) {
            assertTrue("Field " + fieldName + " in class " + clazz.getName() +
                            " does not match " + FIELD_CAMEL_CASE_PATTERN + " or " + CONST_CAMEL_CASE_PATTERN,
                    CONST_CAMEL_CASE_PATTERN.matcher(fieldName).matches() ||
                            FIELD_CAMEL_CASE_PATTERN.matcher(fieldName).matches());
        }
    }
}
