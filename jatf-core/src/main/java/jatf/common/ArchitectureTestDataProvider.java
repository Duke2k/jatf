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

package jatf.common;

import jatf.common.ArchitectureTestConstants.ArchitectureTestConstantsChangeNotifier;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static jatf.common.util.ArchitectureTestUtil.buildReflections;
import static jatf.common.util.ArchitectureTestUtil.resetReflections;

public class ArchitectureTestDataProvider implements Observer {

    private ArchitectureTestRuleEvaluator ruleEvaluator;
    private Map<String, Set<Class<?>>> testClassesMap;

    public ArchitectureTestDataProvider() {
        initializeInstance();
    }

    @Nullable
    public Set<Class<?>> getClassesFor(@Nonnull String testName) {
        return testClassesMap.get(testName);
    }

    @Nullable
    public <A extends Annotation> Annotation getAnnotationFor(Class<?> clazz, Class<A> annotationType) {
        return ruleEvaluator.getAnnotationFor(clazz, annotationType);
    }

    @Override
    public void update(Observable observable, Object argument) {
        if (observable instanceof ArchitectureTestConstantsChangeNotifier) {
            resetReflections();
            initializeInstance();
        }
    }

    private void initializeInstance() {
        Reflections reflections = buildReflections();
        ArchitectureTestAnnotationEvaluator annotationEvaluator =
                new ArchitectureTestAnnotationEvaluator(reflections);
        ruleEvaluator = new ArchitectureTestRuleEvaluator(reflections);
        testClassesMap = newHashMap();
        annotationEvaluator.evaluateInto(testClassesMap);
        ruleEvaluator.evaluateInto(testClassesMap);
    }
}
