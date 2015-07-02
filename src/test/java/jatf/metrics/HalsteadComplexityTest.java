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

package jatf.metrics;

import jatf.common.parser.TokenVisitor;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;

import static jatf.common.ArchitectureTestConstants.MAX_HALSTEAD_DELIVERED_BUGS;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

@RunWith(DataProviderRunner.class)
public class HalsteadComplexityTest extends MetricsTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(HalsteadComplexityTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testHalsteadComplexity(Class<?> clazz) {
        TokenVisitor tokenVisitor = new TokenVisitor();
        parseWithVoidVisitor(clazz, tokenVisitor);
        List<String> halsteadLength = tokenVisitor.getTokens();
        if (halsteadLength.size() > 0) {
            List<String> halsteadVocabulary = tokenVisitor.getUniqueTokens();
            double halsteadVolume =
                    (double) halsteadLength.size() * (Math.log((double) halsteadVocabulary.size()) / Math.log(2));
            double halsteadDeliveredBugs = halsteadVolume / 3000d;
            assertTrue("Halstead complexity threshold violated in class " + clazz.getName(),
                    halsteadDeliveredBugs <= MAX_HALSTEAD_DELIVERED_BUGS);
        }
    }
}
