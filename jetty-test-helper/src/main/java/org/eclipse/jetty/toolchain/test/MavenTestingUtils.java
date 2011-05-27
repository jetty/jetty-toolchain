// ========================================================================
// Copyright (c) Webtide LLC
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
//
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
//
// The Apache License v2.0 is available at
// http://www.apache.org/licenses/LICENSE-2.0.txt
//
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================
package org.eclipse.jetty.toolchain.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * Common utility methods for working with JUnit tests cases in a maven friendly way.
 */
public final class MavenTestingUtils
{
    private static File basedir;
    private static URI baseURI;
    private static File testResourcesDir;
    private static File targetDir;
    
    private MavenTestingUtils()
    {
        /* prevent instantiation */
    }

    public static File getBasedir()
    {
        if (basedir == null)
        {
            String cwd = System.getProperty("basedir");

            if (cwd == null)
            {
                cwd = System.getProperty("user.dir");
            }

            basedir = new File(cwd);
            baseURI = basedir.toURI();
        }

        return basedir;
    }

    /**
     * Get the Basedir for the project as a URI
     * 
     * @return the URI for the project basedir
     */
    public static URI getBaseURI()
    {
        if (baseURI == null)
        {
            getBasedir();
        }
        return baseURI;
    }

    /**
     * Get the directory to the /target directory for this project.
     * 
     * @return the directory path to the target directory.
     */
    public static File getTargetDir()
    {
        if (targetDir == null)
        {
            targetDir = new File(getBasedir(),"target");
            PathAssert.assertDirExists("Target Dir",targetDir);
        }
        return targetDir;
    }

    /**
     * Create a {@link File} object for a path in the /target directory.
     * 
     * @param path
     *            the path desired, no validation of existence is performed.
     * @return the File to the path.
     */
    public static File getTargetFile(String path)
    {
        return new File(getTargetDir(),OS.separators(path));
    }

    /**
     * Get the in <code>/target/tests/</code> code that uses the an arbitrary name.
     * 
     * @return the dir in <code>/target/tests/</code> that uses the an arbitrary name.
     */
    public static File getTargetTestingDir()
    {
        return new File(getTargetDir(),"tests");
    }

    /**
     * Get a dir in /target/ that uses the an arbitrary name.
     * 
     * @param testname
     *            the testname to create directory against.
     * @return the dir in /target/ that uses the an arbitrary name.
     */
    public static File getTargetTestingDir(String testname)
    {
        return new File(getTargetTestingDir(),"test-" + testname);
    }

    /**
     * Get a dir in /target/ that uses the JUnit 3.x {@link TestCase#getName()} to make itself unique.
     * 
     * @param test
     *            the junit 3.x testcase to base this new directory on.
     * @return the dir in /target/ that uses the JUnit 3.x {@link TestCase#getName()} to make itself unique.
     */
    public static File getTargetTestingDir(TestCase test)
    {
        return getTargetTestingDir(test.getName());
    }

    public static URI getTargetURI(String path) throws MalformedURLException
    {
        return getBaseURI().resolve("target/").resolve(path);
    }

    public static URL getTargetURL(String path) throws MalformedURLException
    {
        return getTargetURI(path).toURL();
    }

    /**
     * Get a dir in /target/ that uses the an arbitrary name.
     * <p>
     * Best if used with {@link TestingDir} junit rule.
     * 
     * <pre>
     * &#064;Rule
     * public TestingDir testdir = new TestingDir();
     * 
     * &#064;Test
     * public void testFoo()
     * {
     *     Assert.assertTrue(&quot;Testing dir exists&quot;,testdir.getDir().exists());
     * }
     * </pre>
     * 
     * @param testclass
     *            the class for the test case
     * @param testmethodname
     *            the test method name
     * @return the File path to the testname sepecific testing directory underneath the <code>${basedir}/target</code>
     *         sub directory
     */
    public static File getTargetTestingDir(final Class<?> testclass, final String testmethodname)
    {
        String classname = testclass.getName();
        String methodname = testmethodname;

        classname = condensePackageString(classname);

//        if (OS.IS_WINDOWS)
//        {
//            /* Condense the directory names to make them more friendly for the 
//             * pathname limitations that exist on windows.
//             */
//            methodname = maxStringLength(30,methodname);
//        }

        File testsDir = new File(getTargetDir(),"tests");
        File dir = new File(testsDir,classname + File.separatorChar + methodname);
        FS.ensureDirExists(dir);
        return dir;
    }

    private static class TestID
    {
        public String classname;
        public String methodname;
    }

    public static String getTestIDAsPath()
    {
        TestID id = getTestID();

        id.classname = condensePackageString(id.classname);

//        if (OS.IS_WINDOWS)
//        {
//            /* Condense the directory names to make them more friendly for the 
//             * pathname limitations that exist on windows.
//             */
//            id.methodname = maxStringLength(30,id.methodname);
//        }

        return id.classname + File.separatorChar + id.methodname;
    }

    public static File getProjectFile(String path)
    {
        File file = new File(getBasedir(),OS.separators(path));
        PathAssert.assertFileExists("Project File",file);
        return file;
    }

    public static File getProjectDir(String path)
    {
        File dir = new File(getBasedir(),OS.separators(path));
        PathAssert.assertDirExists("Project Dir",dir);
        return dir;
    }

    /**
     * Using junit 3.x naming standards for unit tests and test method names, attempt to discover the unit test name
     * from the execution stack.
     * 
     * @return the unit test id found via execution stack and junit 3.8 naming conventions.
     * @see #getTestIDAsPath()
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
        Assert.fail(err.toString());
        return null;
    }

    /**
     * Condenses a classname by stripping down the package name to just the first character of each package name
     * segment.
     * <p>
     * 
     * <pre>
     * Examples:
     * "org.eclipse.jetty.test.FooTest"           = "oejt.FooTest"
     * "org.eclipse.jetty.server.logging.LogTest" = "orjsl.LogTest"
     * </pre>
     * 
     * @param classname
     *            the fully qualified class name
     * @return the condensed name
     */
    protected static String condensePackageString(String classname)
    {
        String parts[] = classname.split("\\.");
        StringBuilder dense = new StringBuilder();
        for (int i = 0; i < (parts.length - 1); i++)
        {
            dense.append(parts[i].charAt(0));
        }
        dense.append('.').append(parts[parts.length - 1]);
        return dense.toString();
    }

    /**
     * Smash a long string to fit within the max string length, by taking the middle section of the string and replacing
     * them with an ellipsis "..."
     * <p>
     * 
     * <pre>
     * Examples:
     * .maxStringLength("Eatagramovabits", 5)
     * </pre>
     * 
     * @param max
     *            the maximum size of the string
     * @param raw
     *            the raw string to smash
     * @return the ellipsis'd version of the string.
     */
    protected static String maxStringLength(int max, String raw)
    {
        int length = raw.length();
        if (length <= max)
        {
            return raw;
        }

        return raw.substring(0,3) + "..." + raw.substring((length - max) + 6);
    }

    /**
     * Get a dir from the <code>src/test/resource</code> directory.
     * 
     * @param name
     *            the name of the path to get (it must exist as a dir)
     * @return the dir in <code>src/test/resource</code>
     */
    public static File getTestResourceDir(String name)
    {
        File dir = new File(getTestResourcesDir(),OS.separators(name));
        PathAssert.assertDirExists("Test Resource Dir",dir);
        return dir;
    }

    /**
     * Get a file from the <code>src/test/resource</code> directory.
     * 
     * @param name
     *            the name of the path to get (it must exist as a file)
     * @return the file in <code>src/test/resource</code>
     */
    public static File getTestResourceFile(String name)
    {
        File file = new File(getTestResourcesDir(),OS.separators(name));
        PathAssert.assertFileExists("Test Resource File",file);
        return file;
    }

    /**
     * Get a path resource (File or Dir) from the <code>src/test/resource</code> directory.
     * 
     * @param name
     *            the name of the path to get (it must exist)
     * @return the path in <code>src/test/resource</code>
     */
    public static File getTestResourcePath(String name)
    {
        File path = new File(getTestResourcesDir(),OS.separators(name));
        PathAssert.assertExists("Test Resource Path",path);
        return path;
    }

    /**
     * Get the directory to the <code>src/test/resource</code> directory
     * 
     * @return the directory {@link File} to the <code>src/test/resources</code> directory
     */
    public static File getTestResourcesDir()
    {
        if (testResourcesDir == null)
        {
            testResourcesDir = new File(basedir,OS.separators("src/test/resources"));
            PathAssert.assertDirExists("Test Resources Dir",testResourcesDir);
        }
        return testResourcesDir;
    }

//    /**
//     * Read the contents of a file into a String and return it.
//     * 
//     * @param file
//     *            the file to read.
//     * @return the contents of the file.
//     * @throws IOException
//     *             if unable to read the file.
//     * @deprecated use the call from {@link IO#readToString(File)} instead.
//     */
//    @Deprecated
//    public static String readToString(File file) throws IOException
//    {
//        System.err.printf("DEPRECATED: %s#readToString(File) - use %s#readToString(File) instead to eliminate this warning",
//                MavenTestingUtils.class.getName(), IO.class.getName());
//        return IO.readToString(file);
//    }
}
