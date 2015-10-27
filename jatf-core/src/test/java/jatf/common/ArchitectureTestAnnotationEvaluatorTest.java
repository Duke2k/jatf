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

import org.junit.Test;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static jatf.common.util.ArchitectureTestUtil.buildReflections;
import static org.junit.Assert.assertEquals;

public class ArchitectureTestAnnotationEvaluatorTest {

    @Test
    public void testProcess() {

        // prepare
        Reflections reflections = buildReflections();
        ArchitectureTestAnnotationEvaluator evaluator = new ArchitectureTestAnnotationEvaluator(reflections);

        // test
        evaluator.process();
        Map<String, Set<Class<?>>> targetMap = newHashMap();
        evaluator.evaluateInto(targetMap);

        // verify
        assertEquals(targetMap.size(), 0);
    }
}
