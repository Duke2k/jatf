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

package jatf.dependency;

import com.google.common.io.Files;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.io.SourceFile;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.util.ArchitectureTestUtil.closeQuietly;
import static jatf.common.util.ArchitectureTestUtil.findSourceFileFor;
import static javax.tools.JavaCompiler.CompilationTask;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

@RunWith(DataProviderRunner.class)
public class UncheckedCastsTest extends DependencyTestBase {

  @DataProvider
  public static Object[][] provideClassesToTest() {
    Set<Class<?>> classesToTest = provideClassesFor(UncheckedCastsTest.class);
    return getProvider(classesToTest);
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForSuppressWarningsUncheckedAnnotations(Class<?> clazz) {
    File sourceFile = findSourceFileFor(clazz);
    if (sourceFile != null) {
      try {
        String classSource = Files.toString(sourceFile, Charset.defaultCharset());
        assertFalse(clazz.getName() + " has unchecked casts that are suppressed in the code.",
            classSource.contains("@SuppressWarnings(\"unchecked\")"));
      } catch (IOException e) {
        fail("Source file for " + clazz + " could not be read: " + e);
      }
    }
  }

  @Test
  @UseDataProvider(DATA_PROVIDER_NAME)
  public void testForUncheckedCastsCompilerWarnings(Class<?> clazz) {
    File sourceFile = findSourceFileFor(clazz);
    if (sourceFile != null) {
      Writer writer = new StringWriter();
      CompilationTask task = getCompilationTask(clazz, sourceFile, writer);
      task.call();
      String compilationOutput = writer.toString();
      assertFalse(clazz.getName() + " has unchecked casts that are found during compile.",
          compilationOutput.contains("warning: [unchecked] unchecked conversion"));
      closeQuietly(writer);
    }
  }

  @Nonnull
  private CompilationTask getCompilationTask(Class<?> clazz, @Nonnull File sourceFile, @Nonnull Writer writer) {
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    Set<String> options = newHashSet();
    options.add("-Xlint:unchecked");
    Set<JavaFileObject> javaFileObjects = newHashSet();
    javaFileObjects.add(new SourceFile(sourceFile));
    return compiler.getTask(
        writer,
        null,
        null,
        options,
        Collections.singletonList(clazz.getName()),
        javaFileObjects
    );
  }
}
