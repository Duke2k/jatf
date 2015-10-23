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

package jatf.dependency;

import com.google.common.io.Files;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import jatf.common.graph.Edge;
import jatf.common.graph.Graph;
import jatf.common.graph.Vertex;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;

import static jatf.common.ArchitectureTestConstraints.MAX_DEPTH_FOR_DFS;
import static jatf.common.ArchitectureTestConstraints.SCOPES;
import static jatf.common.util.ArchitectureTestUtil.findSourceFileFor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test checks annotated classes according to the Acyclic Dependencies Principle. In Java, this means to check
 * the import statements of a class and, down to a certain level, also check the import statements of imported classes.
 * We will not check static method imports here for now as well as * imports (which we do not allow anyway).
 * <p/>
 * A second cause of cyclic dependencies is using other classes within a class by field reference. This is checked for
 * cycles as well.
 * <p/>
 * The results are then evaluated as a directed graph which is to be free of cycles ("DependencyTree" only).
 */
@RunWith(DataProviderRunner.class)
public class AcyclicDependenciesPrincipleTest extends DependencyTestBase {

    @DataProvider
    public static Object[][] provideClassesToTest() {
        Set<Class<?>> classesToTest = provideClassesFor(AcyclicDependenciesPrincipleTest.class);
        return getProvider(classesToTest);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testImportStatementsDoNotProduceCycles(Class<?> clazz) {
        GraphBuilder graphBuilder = new GraphBuilder(clazz).invoke();
        Graph<Class<?>> graphOfImports = graphBuilder.getGraph();
        Vertex<Class<?>> start = graphBuilder.getStart();
        buildGraphForImportStatementsIn(clazz, graphOfImports, start, 0);
        Edge<Class<?>>[] cycles = graphOfImports.findCycles();
        assertEquals(cycles.length + " cycles found in import statements of " + clazz.getName(), 0, cycles.length);
    }

    @Test
    @UseDataProvider(DATA_PROVIDER_NAME)
    public void testUsagesDoNotProduceCycles(Class<?> clazz) {
        GraphBuilder graphBuilder = new GraphBuilder(clazz).invoke();
        Graph<Class<?>> graphOfUsages = graphBuilder.getGraph();
        Vertex<Class<?>> start = graphBuilder.getStart();
        buildGraphForUsagesIn(clazz, graphOfUsages, start, 0);
        Edge<Class<?>>[] cycles = graphOfUsages.findCycles();
        assertEquals(cycles.length + " cycles found in usages of other types in " + clazz.getName(), 0, cycles.length);
    }

    private void buildGraphForImportStatementsIn(Class<?> clazz, Graph<Class<?>> graph, Vertex<Class<?>> start, int currentDepth) {
        if (currentDepth <= MAX_DEPTH_FOR_DFS) {
            File sourceFile = findSourceFileFor(clazz);
            List<String> sourceLines;
            try {
                if (sourceFile != null) {
                    sourceLines = Files.readLines(sourceFile, Charset.defaultCharset());
                    for (String line : sourceLines) {
                        if (
                                line.trim().startsWith("import ") &&
                                        !line.trim().startsWith("import static ") &&
                                        !line.trim().endsWith(".*;")
                                ) {
                            Class<?> importedClass = Class.forName(line.substring(7, line.indexOf(";")));
                            if (isInScopes(importedClass)) {
                                Vertex<Class<?>> found = graph.findVertexByName(importedClass.getCanonicalName());
                                if (found == null) {
                                    Vertex<Class<?>> imported = new Vertex<Class<?>>(importedClass.getCanonicalName(), importedClass);
                                    graph.addVertex(imported);
                                    graph.addEdge(start, imported, 1);
                                    buildGraphForImportStatementsIn(importedClass, graph, imported, currentDepth + 1);
                                } else {
                                    graph.addEdge(start, found, 1);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                fail(sourceFile.toString() + " could not be read.");
            } catch (ClassNotFoundException ignored) {
                // on purpose - if class is not found, don't add it to the graph.
            }
        }
    }

    private void buildGraphForUsagesIn(Class<?> clazz, Graph<Class<?>> graph, Vertex<Class<?>> start, int currentDepth) {
        if (currentDepth <= MAX_DEPTH_FOR_DFS) {
            try {
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    Class<?> usedClass = field.getType();
                    if (usedClass.equals(clazz)) {
                        // In case of self-reference, we don't want self-usages to be added to the graph.
                        // This would lead to cycles, definitely. E. g. in Singleton pattern.
                        break;
                    }
                    if (isInScopes(usedClass)) {
                        Vertex<Class<?>> found = graph.findVertexByName(usedClass.getCanonicalName());
                        if (found == null) {
                            Vertex<Class<?>> used = new Vertex<Class<?>>(usedClass.getCanonicalName(), usedClass);
                            graph.addVertex(used);
                            graph.addEdge(start, used, 1);
                            if (findSourceFileFor(usedClass) != null) {
                                buildGraphForUsagesIn(usedClass, graph, used, currentDepth + 1);
                            }
                        } else {
                            graph.addEdge(start, found, 1);
                        }
                    }
                }
            } catch (NoClassDefFoundError ignored) {
                // on purpose - if class is not found, don't add it to the graph.
            }
        }
    }

    private boolean isInScopes(@Nonnull Class<?> clazz) {
        for (String scope : SCOPES) {
            if (clazz.getName().startsWith(scope)) {
                return true;
            }
        }
        return false;
    }

    private class GraphBuilder {
        private Class<?> clazz;
        private Graph<Class<?>> graph;
        private Vertex<Class<?>> start;

        public GraphBuilder(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Graph<Class<?>> getGraph() {
            return graph;
        }

        public Vertex<Class<?>> getStart() {
            return start;
        }

        public GraphBuilder invoke() {
            graph = new Graph<Class<?>>();
            start = new Vertex<Class<?>>(clazz.getCanonicalName(), clazz);
            graph.addVertex(start);
            graph.setRootVertex(start);
            return this;
        }
    }
}
