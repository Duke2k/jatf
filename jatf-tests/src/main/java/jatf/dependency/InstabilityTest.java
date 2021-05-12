/*
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

package jatf.dependency;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.parser.ImportStatementVisitor;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.ArchitectureTestConstraints.INSTABILITY_LOOSE;
import static jatf.common.ArchitectureTestConstraints.INSTABILITY_STRICT;
import static jatf.common.ArchitectureTestRunListener.report;
import static jatf.common.util.ArchitectureTestUtil.buildReflections;
import static jatf.common.util.ArchitectureTestUtil.getAllClassesInReflections;
import static jatf.common.util.ArchitectureTestUtil.getPackageNameFor;
import static jatf.common.util.ArchitectureTestUtil.parseWithVoidVisitor;
import static org.junit.Assert.assertTrue;

/**
 * Instability I = Ce / (Ce + Ca) with Ce being efferent coupling, and Ca being afferent coupling.
 */
@RunWith(DataProviderRunner.class)
public class InstabilityTest extends DependencyTestBase {

  private static Map<String, Set<String>> packageDependencies;

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(InstabilityTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForInstabilityAmongPackages(Class<?> clazz) {
    if (clazz.equals(getClass())) {
      return;
    }
    preparePackageDependencies();
    String currentPackage = clazz.getPackage().getName();
    double ca = (double) countReferencesTo(currentPackage);
    Set<String> currentDependencies = packageDependencies.get(currentPackage);
    double ce;
    if (currentDependencies == null) {
      ce = 0.0;
    } else {
      ce = (double) packageDependencies.get(currentPackage).size();
    }
    if (ce + ca > 0.0) {
      double i = ce / (ce + ca);
      if (i <= INSTABILITY_STRICT) {
        report("Instability surpasses strict value for " + clazz);
      }
      assertTrue("Instability surpasses loose value for " + clazz.getName(), i <= INSTABILITY_LOOSE);
    }
  }

  private void preparePackageDependencies() {
    if (packageDependencies == null) {
      packageDependencies = newHashMap();
      Set<String> classes = getAllClassesInReflections(buildReflections());
      for (String className : classes) {
        String packageName = getPackageNameFor(className);
        if (packageDependencies.get(packageName) == null) {
          Set<String> imported = newHashSet();
          ImportStatementVisitor importStatementVisitor = new ImportStatementVisitor();
          parseWithVoidVisitor(className, importStatementVisitor);
          Set<String> importStatements = importStatementVisitor.getImportStatements();
          for (String importStatement : importStatements) {
            imported.add(getPackageNameFor(importStatement));
          }
          packageDependencies.put(packageName, imported);
        }
      }
    }
  }

  private int countReferencesTo(String packageName) {
    int i = 0;
    for (String p : packageDependencies.keySet()) {
      Set<String> targets = packageDependencies.get(p);
      if (targets.contains(packageName)) {
        i++;
      }
    }
    return i;
  }
}
