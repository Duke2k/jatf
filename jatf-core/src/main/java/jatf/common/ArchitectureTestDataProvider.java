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

import static com.google.common.collect.Maps.newHashMap;
import static jatf.common.ArchitectureTestRunListener.report;
import static jatf.common.util.ArchitectureTestUtil.buildReflections;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.reflections.Reflections;

import com.google.gson.Gson;

public class ArchitectureTestDataProvider {

	private static final String TESTMAP_SNAPSHOT_JSON_FILENAME = "testMapping-%s.json";

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

	private void initializeInstance() {
		Reflections reflections = buildReflections();
		ArchitectureTestAnnotationEvaluator annotationEvaluator =
				new ArchitectureTestAnnotationEvaluator(reflections);
		ruleEvaluator = new ArchitectureTestRuleEvaluator(reflections);
		testClassesMap = newHashMap();
		annotationEvaluator.evaluateInto(testClassesMap);
		ruleEvaluator.evaluateInto(testClassesMap);
		writeTestMappingSnapshot();
	}

	private void writeTestMappingSnapshot() {
		if (ArchitectureTestConstraints.WRITE_TESTMAP_SNAPSHOT_JSON_TO_ROOT_FOLDER) {
			File jsonFile = new File(
					ArchitectureTestConstraints.ROOT_FOLDER,
					String.format(TESTMAP_SNAPSHOT_JSON_FILENAME, System.currentTimeMillis())
			);
			try {
				FileWriter writer = new FileWriter(jsonFile);
				Gson gson = new Gson();
				gson.toJson(testClassesMap, writer);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				report("Could not write test mapping snapshot to " + jsonFile, e);
			}
		}
	}
}
