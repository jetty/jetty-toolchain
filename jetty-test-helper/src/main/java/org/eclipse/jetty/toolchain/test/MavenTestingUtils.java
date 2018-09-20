//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.toolchain.test;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import org.junit.jupiter.api.TestInfo;

/**
 * Common utility methods for working with JUnit tests cases in a maven friendly way.
 */
public final class MavenTestingUtils
{
    private static Path basePath;
    private static Path testResourcesPath;
    private static Path targetPath;

    private MavenTestingUtils()
    {
        /* prevent instantiation */
    }

    /**
     * Obtain a {@link File} reference to the maven ${basedir} for the module.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getBasePath().toFile()</code>
     * 
     * @return the equivalent to the maven ${basedir} property.
     * @see #getBasePath()
     */
    public static File getBaseDir()
    {
        return getBasePath().toFile();
    }

    /**
     * Obtain a {@link Path} reference to the maven ${basedir} for the module.
     * <p>
     * Note: while running in maven, the ${basedir} is populated by maven and used by the surefire-plugin. <br>
     * While running in eclipse, the ${basedir} property is unset, resulting in this method falling back to ${user.dir}
     * equivalent use.
     * 
     * @return the equivalent to the maven ${basedir} property.
     */
    public static Path getBasePath()
    {
        if (basePath == null)
        {
            String cwd = System.getProperty("basedir");

            if (cwd == null)
            {
                cwd = System.getProperty("user.dir");
            }

            try
            {
                basePath = new File(cwd).toPath().toRealPath();
            }
            catch (IOException e)
            {
                // if toRealPath() fails, fallback to as detected version.
                basePath = new File(cwd).getAbsoluteFile().toPath();
            }
        }

        return basePath;
    }

    /**
     * Get the Basedir for the project as a URI
     * 
     * @return the URI for the project basedir
     */
    public static URI getBaseURI()
    {
        return getBasePath().toUri();
    }

    /**
     * Get the {@link File} reference to the <code>/target</code> directory for this project.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetPath().toFile()</code>
     * 
     * @return the directory path to the target directory.
     * @see #getTargetPath()
     */
    public static File getTargetDir()
    {
        return getTargetPath().toFile();
    }

    /**
     * Get the {@link Path} reference to the <code>/target</code> directory for this project.
     * <p>
     * This is roughly equivalent to the <code>${project.build.directory}</code> property.
     * <p>
     * Note: this implementation does not inspect the <code>pom.xml</code> for non-standard locations
     * of the <code>${project.build.directory}</code> property. (it always assumes <code>/target</code>) 
     * 
     * @return the directory path to the <code>/target</code> directory.
     */
    public static Path getTargetPath()
    {
        if (targetPath == null)
        {
            targetPath = getBasePath().resolve("target");
            PathAssert.assertDirExists("Target Dir",targetPath);
        }
        return targetPath;
    }

    /**
     * Create a {@link File} object for a path in the /target directory.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetPath("foo").toFile()</code>
     * 
     * @param path
     *            the path desired, no validation of existence is performed.
     * @return the File to the path.
     * @see #getTargetPath(String)
     */
    public static File getTargetFile(String path)
    {
        return getTargetPath(path).toFile();
    }

    /**
     * Create a {@link Path} object for a path in the /target directory.
     * 
     * @param path
     *            the path desired, no validation of existence is performed.
     * @return the File to the path.
     */
    public static Path getTargetPath(String path)
    {
        Path targetPath = getTargetPath();
        FileSystem fs = targetPath.getFileSystem();
        return fs.getPath(targetPath.toString(),path);
    }
    
    /**
     * Get a {@link File} reference to the maven <code>${basedir}/target/tests/</code> directory.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetTestingPath().toFile()</code>
     * 
     * @return the maven <code>${basedir}/target/tests/</code> directory.
     *         Note: will not validate that the directory exists, or create the directory)
     */
    public static File getTargetTestingDir()
    {
        return getTargetTestingPath().toFile();
    }

    /**
     * Get a {@link Path} reference to the maven <code>${basedir}/target/tests/</code> path.
     * 
     * @return the maven <code>${basedir}/target/tests/</code> directory.
     *         Note: will not validate that the directory exists, or create the directory)
     */
    public static Path getTargetTestingPath()
    {
        return getTargetPath().resolve("tests");
    }

    /**
     * Get a {@link File} reference to the maven <code>${basedir}/target/tests/test-${testname}</code> using
     * the supplied testname
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetTestingPath(testname).toFile()</code>
     * 
     * @param testname
     *            the testname to create directory against.
     * @return the maven <code>${basedir}/target/tests/test-${testname}</code> directory
     */
    public static File getTargetTestingDir(String testname)
    {
        return getTargetTestingPath(testname).toFile();
    }

    /**
     * Get a {@link Path} reference to the maven <code>${basedir}/target/tests/test-${testname}</code> using
     * the supplied testname
     * 
     * @param testname
     *            the testname to create directory against.
     * @return the maven <code>${basedir}/target/tests/test-${testname}</code> directory
     */
    public static Path getTargetTestingPath(String testname)
    {
        return getTargetTestingPath().resolve("test-" + testname);
    }

    /**
     * Get a {@link File} reference to the <code>${basedir}/target/tests/test-${testname}</code> directory.
     * Uses the JUnit 5.x {@link TestInfo#getDisplayName()} to make a unique directory name per test.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetTestingPath(TestCase.getName()).toFile()</code>
     *
     * @param testInfo
     *            the junit 5.x testcase to base this new directory on.
     * @return the maven <code>${basedir}/target/tests/test-${testname}</code> directory.
     */
    public static File getTargetTestingDir(TestInfo testInfo)
    {
        return getTargetTestingPath(testInfo.getDisplayName()).toFile();
    }

    /**
     * Get a {@link Path} reference to the <code>${basedir}/target/tests/test-${testname}</code> directory.
     * Uses the JUnit 5.x {@link TestInfo#getDisplayName()} to make a unique directory name per test.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetTestingPath(TestCase.getName())</code>
     *
     * @param testInfo
     *            the junit 5.x testcase to base this new directory on.
     * @return the maven <code>${basedir}/target/tests/test-${testname}</code> directory.
     * @see #getTargetTestingPath(String)
     */
    public static Path getTargetTestingPath(TestInfo testInfo)
    {
        return getTargetTestingPath(testInfo.getDisplayName());
    }

    /**
     * Get a URI reference to a path (File or Dir) within the maven "${basedir}/target" directory.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetPath(path).toUri()</code>
     * 
     * @param path
     *            the relative path to use
     * @return the URI reference to the target path
     */
    public static URI getTargetURI(String path)
    {
        return getTargetPath(path).toUri();
    }

    /**
     * Get a URL reference to a path (File or Dir) within the maven "${basedir}/target" directory.
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTargetURI(path).toURL()</code>
     * 
     * @param path
     *            the relative path to use
     * @return the URL reference to the target path
     * @throws MalformedURLException
     *             if unable to create a new target url due to URL error.
     */
    public static URL getTargetURL(String path) throws MalformedURLException
    {
        return getTargetURI(path).toURL();
    }

    /**
     * Obtain a testing directory reference in maven
     * <code>${basedir}/target/tests/${condensed-classname}/${methodname}</code> path that uses an condensed directory
     * name based on the testclass and subdirectory based on the testmethod being run.
     *
     * @param testclass
     *            the class for the test case
     * @param testmethodname
     *            the test method name
     * @return the File path to the testname specific testing directory underneath the
     *         <code>${basedir}/target/tests/</code> sub directory
     * @see FS
     */
    public static Path getTargetTestingPath(final Class<?> testclass, final String testmethodname)
    {
        String methodname = testmethodname;

        if (org.junit.jupiter.api.condition.OS.WINDOWS.isCurrentOs())
        {
            /* Condense the directory names to make them more friendly for the 
             * 255 character pathname limitations that exist on windows.
             */
            methodname = StringMangler.maxStringLength(30,methodname);
        }

        Path testdir = getTargetTestingPath().resolve(methodname);
        FS.ensureDirExists(testdir);
        return testdir;
    }

    private static class TestID
    {
        public String classname;
        public String methodname;
    }

    /**
     * Get a {@link File} reference to a required file in the project module path, based on relative
     * path references from maven ${basedir}.
     * <p>
     * Note: will throw assertion error if path does point to an existing file
     * <p>
     * Convenience method for <code>MavenTestingUtils.getProjectFilePath(path).toFile()</code>
     * 
     * @param path
     *            the relative path to reference
     * @return the file reference (must exist)
     */
    public static File getProjectFile(String path)
    {
        return getProjectFilePath(path).toFile();
    }

    /**
     * Get a {@link Path} reference to a required file in the project module path, based on relative
     * path references from maven ${basedir}.
     * <p>
     * Note: will throw assertion error if path does point to an existing file
     * 
     * @param path
     *            the relative path to reference
     * @return the file reference (must exist)
     */
    public static Path getProjectFilePath(String path)
    {
        Path file = getBasePath().resolve(path);
        PathAssert.assertFileExists("Project File",file);
        return file;
    }

    /**
     * Get a directory reference to a required directory in the project module path, based on relative
     * path references from maven ${basedir}.
     * <p>
     * Note: will throw assertion error if path does point to an existing directory
     * <p>
     * Convenience method for <code>MavenTestingUtils.getProjectDirPath(path).toFile()</code>
     * 
     * @param path
     *            the relative path to reference
     * @return the directory reference (must exist)
     */
    public static File getProjectDir(String path)
    {
        return getProjectDirPath(path).toFile();
    }

    /**
     * Get a {@link Path} reference to a required directory in the project module path, based on relative
     * path references from maven ${basedir}.
     * <p>
     * Note: will throw assertion error if path does point to an existing directory
     * 
     * @param path
     *            the relative path to reference
     * @return the directory reference (must exist)
     */
    public static Path getProjectDirPath(String path)
    {
        Path dir = getBasePath().resolve(path);
        PathAssert.assertDirExists("Project Dir",dir);
        return dir;
    }

    /**
     * Using junit 3.x naming standards for unit tests and test method names, attempt to discover the unit test name
     * from the execution stack.
     * 
     * @return the unit test id found via execution stack and junit 3.8 naming conventions.
     */
    private static TestID getTestID()
    {
        StackTraceElement stacked[] = new Throwable().getStackTrace();

        for (StackTraceElement stack : stacked)
        {
            if (stack.getClassName().endsWith("Test"))
            {
                if (stack.getMethodName().startsWith("test"))
                {
                    TestID testid = new TestID();
                    testid.classname = stack.getClassName();
                    testid.methodname = stack.getMethodName();
                    return testid;
                }
            }
        }
        // If we have reached this point, we have failed to find the test id
        String LN = System.getProperty("line.separator");
        StringBuilder err = new StringBuilder();
        err.append("Unable to find a TestID from a testcase that ");
        err.append("doesn't follow the standard naming rules.");
        err.append(LN);
        err.append("Test class name must end in \"*Test\".");
        err.append(LN);
        err.append("Test method name must start in \"test*\".");
        err.append(LN);
        err.append("Call to ").append(MavenTestingUtils.class.getSimpleName());
        err.append(".getTestID(), must occur from within stack frame of ");
        err.append("test method, not @Before, @After, @BeforeClass, ");
        err.append("@AfterClass, or Constructors of test case.");
        fail(err.toString());
        return null;
    }

    /**
     * Get the {@link File} reference to the maven <code>${basedir}/src/test/resources</code> directory
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTestResourcesPath().toFile()</code>
     * 
     * @return the directory {@link File} to the maven <code>${basedir}/src/test/resources</code> directory
     */
    public static File getTestResourcesDir()
    {
        return getTestResourcesPath().toFile();
    }

    /**
     * Get the {@link Path} reference to the maven <code>${basedir}/src/test/resources</code> directory
     * 
     * @return the directory {@link Path} to the maven <code>${basedir}/src/test/resources</code> directory
     */
    public static Path getTestResourcesPath()
    {
        if (testResourcesPath == null)
        {
            testResourcesPath = getBasePath().resolve("src/test/resources");
            PathAssert.assertDirExists("Test Resources Dir",testResourcesPath);
        }
        return testResourcesPath;
    }

    /**
     * Get a dir from the maven <code>${basedir}/src/test/resource</code> directory.
     * <p>
     * Note: will throw assertion error if path does point to an existing directory
     * <p>
     * Convenience method for <code>MavenTestingUtils.getTestResourcesPathDir(name).toFile()</code>
     * 
     * @param name
     *            the name of the path to get (it must exist as a dir)
     * @return the dir in the maven <code>${basedir}/src/test/resource</code> path
     */
    public static File getTestResourceDir(String name)
    {
        return getTestResourcePathDir(name).toFile();
    }

    /**
     * Get a dir from the maven <code>${basedir}/src/test/resource</code> directory.
     * <p>
     * Note: will throw assertion error if path does point to an existing directory
     * 
     * @param name
     *            the name of the path to get (it must exist as a dir)
     * @return the dir in the maven <code>${basedir}/src/test/resource</code> path
     */
    public static Path getTestResourcePathDir(String name)
    {
        Path dir = getTestResourcesPath().resolve(name);
        PathAssert.assertDirExists("Test Resource Dir",dir);
        return dir;
    }

    /**
     * Get a file from the maven <code>${basedir}/src/test/resource</code> directory.
     * <p>
     * Note: will throw assertion error if path does point to an existing file
     * 
     * @param name
     *            the name of the path to get (it must exist as a file)
     * @return the file in maven <code>${basedir}/src/test/resource</code>
     */
    public static File getTestResourceFile(String name)
    {
        File file = new File(getTestResourcesDir(),FS.separators(name));
        PathAssert.assertFileExists("Test Resource File",file);
        return file;
    }

    /**
     * Get a file from the maven <code>${basedir}/src/test/resource</code> directory.
     * <p>
     * Note: will throw assertion error if path does point to an existing file
     * 
     * @param name
     *            the name of the path to get (it must exist as a file)
     * @return the file in maven <code>${basedir}/src/test/resource</code>
     */
    public static Path getTestResourcePathFile(String name)
    {
        Path file = getTestResourcesPath().resolve(name);
        PathAssert.assertFileExists("Test Resource File",file);
        return file;
    }

    /**
     * Get a path resource (File or Dir) from the maven <code>${basedir}/src/test/resource</code> directory.
     * 
     * @param name
     *            the name of the path to get (it must exist)
     * @return the path in maven <code>${basedir}/src/test/resource</code>
     */
    public static Path getTestResourcePath(String name)
    {
        Path path = getTestResourcesPath().resolve(name);
        PathAssert.assertPathExists("Test Resource Path",path);
        return path;
    }
}
