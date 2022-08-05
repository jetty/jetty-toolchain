//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.toolchain.test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.eclipse.jetty.toolchain.test.PathMatchers.isDirectory;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isRegularFile;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Simpler replacement for {@link MavenTestingUtils} used to find paths within the Maven <code>${project.basedir}</code>
 */
public final class MavenPaths
{
    private static Path basePath;

    /**
     * Get the equivalent of <code>${project.basedir}</code> (what used to just be called <code>${basedir}</code>
     *
     * @return the project basedir
     */
    public static Path projectBase()
    {
        if (basePath == null)
        {
            // Test known properties if running from a maven scope (surefire, failsafe, invoker, exec, etc)
            // first, then fallback to CWD (Current Working Directory) if prior ones fail.
            // Modern name first, then older name
            for (String propName : List.of("project.basedir", "basedir", "user.dir"))
            {
                String propDir = System.getProperty(propName);
                if (propDir != null)
                {
                    Path dir = Paths.get(propDir);
                    if (Files.exists(dir) && Files.isDirectory(dir))
                    {
                        basePath = absolute(dir);
                        return basePath;
                    }
                }
            }
            throw new IllegalStateException("Java has no Current Working Directory: System.getProperty(\"user.dir\") = " + System.getProperty("user.dir"));
        }

        return basePath;
    }

    /**
     * Get the {@link Path} reference to the <code>/target</code> directory for this project.
     * <p>
     * This is roughly equivalent to the <code>${project.build.directory}</code> property.
     * </p>
     * <p>
     * Note: this implementation does not inspect the <code>pom.xml</code> for non-standard locations
     * of the <code>${project.build.directory}</code> property, nor does it actually use the
     * <code>${project.build.directory}</code>.
     * </p>
     * <p>
     * This is a convenience method for <code>MavenPaths.projectBase().resolve("target")</code>
     * but ensures that the directory exists before returning it.
     * </p>
     *
     * @return the directory path to the <code>/target</code> directory.
     */
    public static Path targetDir()
    {
        Path targetDir = projectBase().resolve("target");
        assertThat("Target Dir", targetDir, isDirectory());
        return targetDir;
    }

    public static Path targetTests()
    {
        Path testsDir = targetDir().resolve("tests");
        if (!Files.exists(testsDir))
        {
            try
            {
                Files.createDirectory(testsDir);
            }
            catch (IOException e)
            {
                throw new IllegalStateException("Unable to create required directory: " + testsDir, e);
            }
        }
        assertThat("Tests Dir", testsDir, isDirectory());
        return testsDir;
    }

    /**
     * Resolve the unique test directory based on {@link ExtensionContext}
     *
     * @param context the {@link ExtensionContext} to build <code>target/tests/{name}</code> from
     * @return the unique test path for the context
     */
    public static Path targetTestDir(ExtensionContext context)
    {
        Objects.requireNonNull(context);
        if (!context.getTestInstance().isPresent())
            throw new AssertionError("ExtensionContext is invalid");

        String methodName = null;
        if (context.getTestMethod().isPresent())
            methodName = context.getTestMethod().get().getName();

        Object obj = context.getTestInstance().get();
        Class<?> testClazz = context.getTestClass().orElse(obj.getClass());

        return targetTestDir(testClazz, methodName, context.getDisplayName());
    }

    /**
     * Resolve the unique test directory based on {@link TestInfo}
     *
     * @param info the {@link TestInfo} to build <code>target/tests/{name}</code> from
     * @return the unique test path for the context
     */
    public static Path targetTestDir(TestInfo info)
    {
        Objects.requireNonNull(info);
        if (info.getTestClass().isPresent())
            throw new AssertionError("TestInfo is invalid");

        String methodName = null;
        if (info.getTestMethod().isPresent())
            methodName = info.getTestMethod().get().getName();

        Class<?> testClazz = info.getTestClass().get();

        return targetTestDir(testClazz, methodName, info.getDisplayName());
    }

    /**
     * Resolve the test directory
     *
     * @param name the to resolve <code>target/tests/{name}</code> from
     * @return the test path for the context
     */
    public static Path targetTestDir(String name)
    {
        Path path = targetTests().resolve(name);
        // ensure that if it exists, it's a directory
        if (Files.exists(path))
            assertThat("Test Directory", path, isDirectory());
        return path;
    }

    /**
     * Internal test directory name creation logic.
     * <p>
     * This creates a name under <code>target/tests/{clazzName_condensed}/{name}</code>
     * where <code>{name}</code> is either <code>{methodName}{displayName}</code>
     * or <code>{displayName}</code> depending on if <code>{methodName}</code> exists
     * or not.
     * </p>
     *
     * @param testClazz the test class name
     * @param methodName the method name (usually from {@link ExtensionContext#getTestMethod()} or {@link TestInfo#getTestMethod()})
     * @param displayName the display name (usually from {@link ExtensionContext#getDisplayName()} or {@link TestInfo#getDisplayName()})
     */
    public static Path targetTestDir(Class<?> testClazz, String methodName, String displayName)
    {
        Objects.requireNonNull(testClazz, "Class name");
        Objects.requireNonNull(displayName, "Display name");

        StringBuilder dirName = new StringBuilder();

        dirName.append(StringMangler.condensePackageString(testClazz.getName()));
        dirName.append(File.separatorChar);

        if (methodName != null)
        {
            if (OS.WINDOWS.isCurrentOs())
            {
                dirName.append(StringMangler.maxStringLength(30, methodName));
            }
            else
            {
                dirName.append(methodName);
            }

            if (!displayName.startsWith(methodName))
            {
                dirName.append(safename(displayName.trim()));
            }
        }
        else
        {
            dirName.append(safename(displayName.trim()));
        }

        return targetTestDir(dirName.toString());
    }

    /**
     * <p>
     * Search for an expected file in the equivalent of the <code>src/main</code> tree.
     * </p>
     * <p>
     * Search order:
     *   <ol>
     *       <li><code>src/main/resources/${name}</code></li>
     *       <li><code>target/classes/${name}</code></li>
     *   </ol>
     * </p>
     * <p>
     *     If name exists anywhere in the search tree, but is not a File, this
     *     results in an {@link AssertionError}
     * </p>
     *
     * @param name the name to search for (must not start with {@code /}. use only URI path separator syntax)
     * @return the Path (always a regular file)
     * @throws AssertionError if name does not exist, or resulting path is not a File
     */
    public static Path findMainResourceFile(String name)
    {
        Path path = findMainResource(name);
        String msg = String.format("findMainResourceFile(\"%s\")", name);
        assertThat(msg, path, isRegularFile());
        return path;
    }

    /**
     * <p>
     * Search for an expected directory in the equivalent of the <code>src/main</code> tree.
     * </p>
     * <p>
     * Search order:
     *   <ol>
     *       <li><code>src/main/resources/${name}</code></li>
     *       <li><code>target/classes/${name}</code></li>
     *   </ol>
     * </p>
     * <p>
     *     If name exists anywhere in the search tree, but is not a Directory, this
     *     results in an {@link AssertionError}
     * </p>
     *
     * @param name the name to search for (must not start with {@code /}. use only URI path separator syntax)
     * @return the Path (always a directory)
     * @throws AssertionError if name does not exist, or resulting path is not a directory
     */
    public static Path findMainResourceDir(String name)
    {
        Path path = findMainResource(name);
        String msg = String.format("findMainResourceDir(\"%s\")", name);
        assertThat(msg, path, isDirectory());
        return path;
    }

    /**
     * <p>
     * Search for an expected file in the equivalent of the <code>src/test</code> tree.
     * </p>
     * <p>
     * Search order:
     *   <ol>
     *       <li><code>src/test/resources/${name}</code></li>
     *       <li><code>target/test-classes/${name}</code></li>
     *       <li><code>ClassLoader.getResource(${name})</code></li>
     *   </ol>
     * </p>
     * <p>
     *     If name exists anywhere in the search tree, but is not a File, this
     *     results in an {@link AssertionError}
     * </p>
     *
     * @param name the name to search for (must not start with {@code /}. use only URI path separator syntax)
     * @return the Path (always a regular file)
     * @throws AssertionError if name does not exist, or resulting path is not a File
     */
    public static Path findTestResourceFile(String name)
    {
        Path path = findTestResource(name);
        String msg = String.format("findTestResourceFile(\"%s\")", name);
        assertThat(msg, path, isRegularFile());
        return path;
    }

    /**
     * <p>
     * Search for an expected file in the equivalent of the <code>src/main</code> tree.
     * </p>
     * <p>
     * Search order:
     *   <ol>
     *       <li><code>src/main/resources/${name}</code></li>
     *       <li><code>target/classes/${name}</code></li>
     *       <li><code>ClassLoader.getResource(${name})</code></li>
     *   </ol>
     * </p>
     * <p>
     *     If name exists anywhere in the search tree, but is not a Directory, this
     *     results in an {@link AssertionError}
     * </p>
     *
     * @param name the name to search for (must not start with {@code /}. use only URI path separator syntax)
     * @return the Path (always a directory)
     * @throws AssertionError if name does not exist, or resulting path is not a Directory
     */
    public static Path findTestResourceDir(String name)
    {
        Path path = findTestResource(name);
        String msg = String.format("findTestResourceDir(\"%s\")", name);
        assertThat(msg, path, isDirectory());
        return path;
    }

    private static Path absolute(Path path)
    {
        Path result;
        try
        {
            result = path.toRealPath();
        }
        catch (IOException e)
        {
            // if toRealPath() fails, fallback to as detected version.
            result = path.toAbsolutePath();
        }
        return result;
    }

    private static Path findMainResource(String name)
    {
        Path srcMainPath = projectBase().resolve("src/main/resources/" + name);
        if (Files.exists(srcMainPath))
        {
            return srcMainPath;
        }

        Path targetClassesPath = projectBase().resolve("target/classes/" + name);
        if (Files.exists(targetClassesPath))
        {
            return targetClassesPath;
        }

        return null;
    }

    private static Path findTestResource(String name)
    {
        Path srcMainPath = projectBase().resolve("src/test/resources/" + name);
        if (Files.exists(srcMainPath))
        {
            return srcMainPath;
        }

        Path targetClassesPath = projectBase().resolve("target/test-classes/" + name);
        if (Files.exists(targetClassesPath))
        {
            return targetClassesPath;
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL classLoaderUrl = classLoader.getResource(name);
        if (classLoaderUrl != null)
        {
            URI uri = URI.create(classLoaderUrl.toExternalForm());
            // only supports "file:///" based locations (not "jar:file:///")
            if ("file".equals(uri.getScheme()))
            {
                Path classLoaderPath = Paths.get(uri);
                if (Files.exists(classLoaderPath))
                {
                    return classLoaderPath;
                }
            }
        }

        return null;
    }

    static String safename(String name)
    {
        final String reserved = "<>:\"|?*";
        StringBuilder result = new StringBuilder();
        for (char c : name.toCharArray())
        {
            if (Character.isISOControl(c) || reserved.indexOf(c) >= 0)
            {
                result.append(String.format("%02X", (byte)(c & 0xFF))); // replacement char
            }
            else
            {
                result.append(c);
            }
        }
        return result.toString();
    }
}
