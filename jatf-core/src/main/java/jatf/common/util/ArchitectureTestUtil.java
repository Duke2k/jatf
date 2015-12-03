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

package jatf.common.util;

import com.google.common.collect.Multimap;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.expr.AssignExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;
import jatf.annotations.Dependency;
import jatf.annotations.Pattern;
import jatf.common.rules.markers.ArchitectureTestMarker;
import jatf.common.rules.markers.ExcludeMarker;
import jatf.common.rules.markers.MustBePureMarker;
import jatf.common.rules.markers.MustExtendMarker;
import jatf.common.rules.markers.MustHaveAnnotationMarker;
import jatf.common.rules.markers.MustImplementMarker;
import jatf.common.rules.markers.MustNotExtendMarker;
import jatf.common.rules.markers.MustNotHaveAnnotationMarker;
import jatf.common.rules.markers.MustNotImplementMarker;
import jatf.common.rules.markers.MustNotOverrideMarker;
import jatf.common.rules.markers.MustNotReturnMarker;
import jatf.common.rules.markers.MustNotUseMarker;
import jatf.common.rules.markers.MustOverrideMarker;
import jatf.common.rules.markers.MustReturnMarker;
import jatf.common.rules.markers.MustUseMarker;
import jatf.common.rules.markers.RuleBasedMarker;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static jatf.common.ArchitectureTestConstraints.ROOT_FOLDER;
import static jatf.common.ArchitectureTestConstraints.SCOPES;
import static jatf.common.ArchitectureTestRunListener.report;

public class ArchitectureTestUtil {

    private static Map<String, String> sourceFiles;
    private static Set<URL> targetFolderUrls;
    private static Reflections reflections;

    @Nonnull
    public static Reflections buildReflections() {
        if (reflections == null) {
            initTargetFolderUrlSetIfNecessary();
            for (String scope : SCOPES) {
                targetFolderUrls.addAll(new Reflections(scope).getConfiguration().getUrls());
            }
            targetFolderUrls.addAll(new Reflections("jatf").getConfiguration().getUrls());
            URLClassLoader urlClassLoader =
                    URLClassLoader.newInstance(targetFolderUrls.toArray(new URL[targetFolderUrls.size()]));
            reflections = new Reflections(new ConfigurationBuilder()
                    .setUrls(targetFolderUrls)
                    .addClassLoader(urlClassLoader)
            );
        }
        return reflections;
    }

    public static void resetReflections() {
        targetFolderUrls = null;
        reflections = null;
        buildReflections();
    }

    public static void resetSourceFilesMap() {
        sourceFiles = null;
        try {
            initSourceFilesMapIfNecessary();
        } catch (IOException e) {
            report("Source files could not be mapped:", e);
        }
    }

    @Nonnull
    public static Set<String> getAllClassesInReflections(Reflections reflections) {
        Set<String> result = newHashSet();
        for (String key : reflections.getStore().keySet()) {
            Multimap<String, String> multimap = reflections.getStore().get(key);
            for (String name : multimap.keySet()) {
                Collection<String> collection = multimap.get(name);
                for (String item : collection) {
                    if (startsWithAnyOf(SCOPES, item)) {
                        result.add(item);
                    }
                }
            }
        }
        return result;
    }

    @Nonnull
    public static String assertMessage(@Nonnull Class<?> type) {
        return "Assertion not met in " + type.getName();
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    @Nullable
    public static File findSourceFileFor(@Nonnull Class<?> clazz) {
        return findSourceFileFor(clazz.getName());
    }

    @Nullable
    public static File findSourceFileFor(@Nonnull String className) {
        try {
            initSourceFilesMapIfNecessary();
        } catch (IOException e) {
            report("Source files could not be mapped:", e);
            return null;
        }
        String path = sourceFiles.get(className);
        return path == null ? null : new File(path);
    }

    @SuppressWarnings("unused")
    @Nonnull
    public static <T> Set<Class<?>> getSubTypesOf(@Nonnull Class<T> superclass) {
        Set<Class<?>> entities = newHashSet();
        Reflections reflections = buildReflections();
        entities.addAll(reflections.getSubTypesOf(superclass));
        return entities;
    }

    @Nonnull
    public static String getPackageNameFor(@Nonnull String className) {
        return className.substring(0, className.lastIndexOf("."));
    }

    private static void initTargetFolderUrlSetIfNecessary() {
        if (targetFolderUrls == null) {
            targetFolderUrls = newHashSet();
            try {
                File path = new File(ROOT_FOLDER);
                report("Checking '" + path.getAbsolutePath() +
                        "' for classes...");
                traverseForTargetFolderUrlsIn(path.getAbsolutePath(), targetFolderUrls);
            } catch (MalformedURLException e) {
                report("Failed to scan for targets paths:", e);
            }
            report("Added " + targetFolderUrls.size() + " locations for .class files to reflections repository.");
        }
    }

    private static void traverseForTargetFolderUrlsIn(@Nonnull String path, @Nonnull Set<URL> urls) throws MalformedURLException {
        File root = new File(path);
        File[] list = root.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isDirectory()) {
                    if (file.getName().equalsIgnoreCase("classes") &&
                            file.getParentFile() != null && file.getParentFile().exists() &&
                            file.getParentFile().getName().equalsIgnoreCase("target")) {
                        urls.add(file.toURI().toURL());
                    } else {
                        traverseForTargetFolderUrlsIn(file.getAbsolutePath(), urls);
                    }
                }
            }
        }
    }

    private static void initSourceFilesMapIfNecessary() throws IOException {
        if (sourceFiles == null) {
            sourceFiles = newHashMap();
            File path = new File(ROOT_FOLDER);
            report("Checking '" + path.getAbsolutePath() +
                    "' for source files...");
            traverseForJavaFilesIn(path.getAbsolutePath(), sourceFiles);
            report("Added " + sourceFiles.entrySet().size() + " files to source repository.");
        }
    }

    private static void traverseForJavaFilesIn(@Nonnull String path, @Nonnull Map<String, String> sourceFiles) throws IOException {
        File root = new File(path);
        File[] list = root.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isDirectory()) {
                    traverseForJavaFilesIn(file.getAbsolutePath(), sourceFiles);
                } else {
                    String fileName = file.getName();
                    if (fileName.endsWith(".java")) {
                        sourceFiles.put(getClassNameFor(file), file.getAbsolutePath());
                    }
                }
            }
        }
    }

    @Nonnull
    private static String getClassNameFor(@Nonnull File file) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            String sourceLine;
            String packageKeyword = "package ";
            while ((sourceLine = reader.readLine()) != null) {
                int indexOfPackage = sourceLine.indexOf(packageKeyword);
                int indexOfSemicolon = sourceLine.indexOf(";", indexOfPackage);
                if (indexOfPackage >= 0 && indexOfSemicolon >= packageKeyword.length()) {
                    result.append(sourceLine.substring(indexOfPackage + packageKeyword.length(), indexOfSemicolon));
                    result.append(".");
                    break;
                }
            }
            String fileName = file.getName();
            result.append(fileName.substring(0, fileName.indexOf('.')));
        } finally {
            reader.close();
        }
        return result.toString();
    }

    @Nullable
    public static <M extends RuleBasedMarker> M createAnnotation(@Nonnull Class<M> markerType, final Map<String, List<?>> fields) {
        M marker = null;
        try {
            marker = markerType.newInstance();
            if (marker instanceof ArchitectureTestMarker) {
                ((ArchitectureTestMarker) marker).omitConventions =
                        Boolean.parseBoolean(fields.get("omitConventions").get(0).toString());
                ((ArchitectureTestMarker) marker).omitMetrics =
                        Boolean.parseBoolean(fields.get("omitMetrics").get(0).toString());
                ((ArchitectureTestMarker) marker).dependencies = convertToDependencyArray(fields.get("dependencies"));
                ((ArchitectureTestMarker) marker).patterns = convertToPatternArray(fields.get("patterns"));
            } else if (marker instanceof MustBePureMarker) {
                ((MustBePureMarker) marker).degree = Double.parseDouble(fields.get("degree").get(0).toString());
            } else if (marker instanceof MustExtendMarker) {
                ((MustExtendMarker) marker).type = (Class<?>) fields.get("type").get(0);
            } else if (marker instanceof MustImplementMarker) {
                ((MustImplementMarker) marker).interfaces = convertToTypeArray(fields.get("interfaces"));
            } else if (marker instanceof MustNotExtendMarker) {
                ((MustNotExtendMarker) marker).type = (Class<?>) fields.get("type").get(0);
            } else if (marker instanceof MustNotImplementMarker) {
                ((MustNotImplementMarker) marker).interfaces = convertToTypeArray(fields.get("interfaces"));
            } else if (marker instanceof MustNotOverrideMarker) {
                ((MustNotOverrideMarker) marker).methodNames = convertToStringArray(fields.get("methodNames"));
            } else if (marker instanceof MustNotUseMarker) {
                ((MustNotUseMarker) marker).types = convertToTypeArray(fields.get("types"));
            } else if (marker instanceof MustOverrideMarker) {
                ((MustOverrideMarker) marker).methodNames = convertToStringArray(fields.get("methodNames"));
            } else if (marker instanceof MustUseMarker) {
                ((MustUseMarker) marker).types = convertToTypeArray(fields.get("types"));
            } else if (marker instanceof MustHaveAnnotationMarker) {
                //noinspection unchecked
                ((MustHaveAnnotationMarker) marker).annotation = (Class<? extends Annotation>) fields.get("annotation").get(0);
            } else if (marker instanceof MustNotHaveAnnotationMarker) {
                //noinspection unchecked
                ((MustNotHaveAnnotationMarker) marker).annotation = (Class<? extends Annotation>) fields.get("annotation").get(0);
            } else if (marker instanceof MustReturnMarker) {
                ((MustReturnMarker) marker).type = (Class<?>) fields.get("type").get(0);
            } else if (marker instanceof MustNotReturnMarker) {
                ((MustNotReturnMarker) marker).type = (Class<?>) fields.get("type").get(0);
            } else if (marker instanceof ExcludeMarker) {
                ((ExcludeMarker) marker).tests = convertToTypeArray(fields.get("tests"));
            }
        } catch (Exception e) {
            report("RuleBasedAnnotation " + markerType + " could not be created:", e);
        }
        return marker;
    }

    @Nullable
    public static <M extends RuleBasedMarker> M createAnnotation(@Nonnull Class<M> markerType, final String argumentName, final List<?> values) {
        Map<String, List<?>> fields = newHashMap();
        fields.put(argumentName, values);
        return createAnnotation(markerType, fields);
    }

    @Nullable
    public static ArchitectureTestMarker createArchitectureTestAnnotation(
            boolean omitMetrics,
            boolean omitConventions,
            Pattern[] patterns,
            Dependency[] dependencies
    ) {
        return createAnnotation(ArchitectureTestMarker.class, fieldsForArchitectureTestAnnotation(
                omitMetrics,
                omitConventions,
                patterns,
                dependencies
        ));
    }

    public static <V extends VoidVisitorAdapter<?>> void parseWithVoidVisitor(@Nonnull Class<?> clazz, @Nonnull V visitor) {
        File sourceFile = findSourceFileFor(clazz);
        if (sourceFile != null) {
            parseWithVoidVisitor(clazz.getName(), visitor, sourceFile);
        }
    }

    public static <V extends VoidVisitorAdapter<?>> void parseWithVoidVisitor(@Nonnull String className, @Nonnull V visitor) {
        File sourceFile = findSourceFileFor(className);
        if (sourceFile != null) {
            parseWithVoidVisitor(className, visitor, sourceFile);
        }
    }

    private static <V extends VoidVisitorAdapter<?>> void parseWithVoidVisitor(String className, V visitor, File sourceFile) {
        try {
            CompilationUnit compilationUnit = JavaParser.parse(sourceFile);
            //noinspection unchecked
            visitor.visit(compilationUnit, null);
        } catch (ParseException e) {
            report("Could not parse source file for class " + className, e);
        } catch (IOException e) {
            report("Could not open source file for class " + className, e);
        }
    }

    public static <V extends VoidVisitorAdapter<?>> void parseWithVoidVisitor(@Nonnull BlockStmt blockStmt, @Nonnull V visitor) {
        visitor.visit(blockStmt, null);
    }

    public static <V extends VoidVisitorAdapter<?>> void parseWithVoidVisitor(@Nonnull AssignExpr assignExpr, @Nonnull V visitor) {
        visitor.visit(assignExpr, null);
    }

    @Nonnull
    private static Map<String, List<?>> fieldsForArchitectureTestAnnotation(
            boolean omitMetrics,
            boolean omitConventions,
            Pattern[] patterns,
            Dependency[] dependencies
    ) {
        Map<String, List<?>> fields = newHashMap();
        fields.put("omitMetrics", Collections.singletonList(omitMetrics));
        fields.put("omitConventions", Collections.singletonList(omitConventions));
        fields.put("patterns", Arrays.asList(patterns));
        fields.put("dependencies", Arrays.asList(dependencies));
        return fields;
    }

    private static String[] convertToStringArray(List<?> items) {
        String[] result = new String[items.size()];
        int i = 0;
        for (Object object : items) {
            result[i++] = (String) object;
        }
        return result;
    }

    private static Class<?>[] convertToTypeArray(List<?> items) {
        Class<?>[] result = new Class<?>[items.size()];
        int i = 0;
        for (Object object : items) {
            result[i++] = (Class<?>) object;
        }
        return result;
    }

    private static Pattern[] convertToPatternArray(List<?> items) {
        Pattern[] result = new Pattern[items.size()];
        int i = 0;
        for (Object object : items) {
            result[i++] = (Pattern) object;
        }
        return result;
    }

    private static Dependency[] convertToDependencyArray(List<?> items) {
        Dependency[] result = new Dependency[items.size()];
        int i = 0;
        for (Object object : items) {
            result[i++] = (Dependency) object;
        }
        return result;
    }

    private static boolean startsWithAnyOf(String[] prefixes, @Nonnull String s) {
        for (String prefix : prefixes) {
            if (s.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
