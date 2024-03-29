/*
  This file is part of JATF.
  <p>
  JATF is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, version 3 of the License.
  <p>
  JATF is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with JATF.  If not, see <http://www.gnu.org/licenses/>.
 */

package jatf.common;

import jatf.annotations.ArchitectureTest;
import jatf.api.rules.ArchitectureTestRuleGenerator;
import jatf.common.rules.AnnotationBasedRule;
import jatf.common.rules.ArchitectureTestRule;
import jatf.common.rules.markers.ArchitectureTestMarker;
import jatf.common.rules.markers.RuleBasedMarker;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.ArchitectureTestRunListener.report;

@SuppressWarnings("WeakerAccess")
public class ArchitectureTestRuleEvaluator extends ArchitectureTestAbstractEvaluator {

  private final Map<Class<?>, Set<RuleBasedMarker<? extends Annotation>>> markerMap;

  public ArchitectureTestRuleEvaluator(@Nonnull Reflections reflections) {
    super(reflections);
    markerMap = newHashMap();
  }

  @Nullable
  private static <T> T findFirst(@Nonnull Set<?> set) {
    for (Object item : set) {
      if (item.getClass().equals(ArchitectureTestMarker.class)) {
        //noinspection unchecked
        return (T) item;
      }
    }
    return null;
  }

  @Nullable
  public Annotation getAnnotationFor(@Nonnull Class<?> clazz, @Nonnull Class<? extends Annotation> annotationType) {
    String annotationName = annotationType.getSimpleName();
    Set<Annotation> annotations = getAnnotationsFor(clazz);
    for (Annotation annotation : annotations) {
      if (annotation.toString().equals(annotationName)) {
        return annotation;
      }
    }
    return null;
  }

  @SuppressWarnings("rawtypes")
  @Override
  protected void process() {
    Set<Class<? extends ArchitectureTestRuleGenerator>> ruleGeneratorTypes =
        reflections.getSubTypesOf(ArchitectureTestRuleGenerator.class);
    for (Class<? extends ArchitectureTestRuleGenerator> ruleGeneratorType : ruleGeneratorTypes) {
      ArchitectureTestRuleGenerator ruleGenerator = null;
      try {
        ruleGenerator = ruleGeneratorType.getDeclaredConstructor().newInstance();
        AnnotationBasedRule rule1 = ruleGenerator.generateRule();
        RuleBasedMarker marker1 = rule1.getMarker();
        for (Class<?> clazz : rule1.getClasses()) {
          Set<RuleBasedMarker<? extends Annotation>> markerSet = markerMap.get(clazz);
          if (markerSet == null) {
            markerSet = newHashSet();
          }
          //noinspection unchecked
          markerSet.add(marker1);
        }
        ArchitectureTestRule rule2 = ruleGenerator.generateArchitectureTestRule();
        ArchitectureTestMarker marker2 = rule2.getMarker();
        for (Class<?> clazz : rule2.getClasses()) {
          Set<RuleBasedMarker<? extends Annotation>> markerSet = markerMap.get(clazz);
          if (markerSet == null) {
            markerSet = newHashSet();
          }
          ArchitectureTestMarker old = findFirst(markerSet);
          if (old != null) {
            marker2 = marker2.merge(old);
            markerSet.remove(old);
          }
          markerSet.add(marker2);
          markerMap.put(clazz, markerSet);
        }
      } catch (Exception e) {
        report("Failed to instantiate rule generator" +
            (ruleGenerator != null ? " " + ruleGenerator.getClass().getName() : "") + ".", e);
      }
    }
    for (Class<?> clazz : markerMap.keySet()) {
      ArchitectureTestMarker marker = findFirst(markerMap.get(clazz));
      if (marker != null) {
        ArchitectureTest annotation = marker.createAnnotation();
        addAnnotatedClass(clazz, annotation);
      }
    }
  }

  @Nonnull
  private Set<Annotation> getAnnotationsFor(@Nonnull Class<?> clazz) {
    Set<RuleBasedMarker<? extends Annotation>> markerSet = markerMap.get(clazz);
    Set<Annotation> annotationSet = newHashSet();
    if (markerSet != null) {
      annotationSet = markerSet.stream()
          .map(RuleBasedMarker::createAnnotation)
          .collect(Collectors.toSet());
    }
    return annotationSet;
  }
}
