//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A junit 4.x {@link org.junit.Rule} to provide a common, easy to use, testing directory that is unique per unit test method.
 * <p>
 * Similar in scope to the {@link org.junit.rules.TemporaryFolder} rule, as it creates a directory, when asked (via {@link #getDir()} or {@link #getEmptyDir()}) in the maven
 * project familiar and friendly maven location of <code>${basedir}/target/tests/${testclass}/${testmethod}</code>.
 * <p>
 * Note: {@link MavenTestingUtils} will keep the directory name short for the sake of windows users.
 * <p>
 * This is best suited for Tests that will benefit from a unit directory per test method.
 * If you desire to have a test directory per test class, with all test methods sharing the same test directory, then
 * consider using the {@link MavenTestingUtils#getTargetTestingDir(String)}
 * 
 * <p>
 * Example use:
 * 
 * <pre>
 * public class TestingDirTest
 * {
 *     &#064;Rule
 *     public TestingDir testingdir = new TestingDir();
 * 
 *     &#064;Test
 *     public void testUseDirectory()
 *     {
 *         File appDir = testingdir.getFile("app");
 *         File tmpDir = testingdir.getFile("tmp");
 *         
 *         FS.ensureDirEmpty(appDir);
 *         FS.ensureDirEmpty(tmpDir);
 *         
 *         File index = new File(appDir, "index.html");
 *         FileWriter writer = new FileWriter(index);
 *         writer.write("Hello World");
 *         writer.close();
 *         
 *         Server server = new Server();
 *         server.setTmpDir(tmpDir);
 *         server.addWebApp(appDir);
 *         server.start();
 *         
 *         Client client = new Client();
 *         String response = client.request("http://localhost/app/");
 * 
 *         Assert.assertThat(response,containsString("Hello World"));
 *     }
 * }
 * </pre>
 */
public class TestingDir implements TestRule
{
    private Path dir;

    public Statement apply(final Statement statement, final Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                dir = MavenTestingUtils.getTargetTestingPath(description.getTestClass(),description.getMethodName()).toRealPath();
                FS.ensureEmpty(dir);
                statement.evaluate();
            }
        };
    }

    /**
     * Get the test specific directory to use for testing work directory.
     * <p>
     * Name is derived from the test classname &amp; method name.
     * 
     * @return the test specific directory.
     */
    public Path getPath()
    {
        if (Files.exists(dir))
        {
            return dir;
        }

        FS.ensureDirExists(dir);
        return dir;
    }

    /**
     * Get the test specific directory to use for testing work directory.
     * <p>
     * Name is derived from the test classname &amp; method name.
     * 
     * @return the test specific directory.
     * @deprecated use <code>javax.nio.file</code> replacement {@link #getPath()} instead
     */
    @Deprecated
    public File getDir()
    {
        return getPath().toFile();
    }

    /**
     * Get a file inside of the test specific test directory.
     * <p>
     * Note: No assertions are made if the file exists or not.
     * 
     * @param name
     *            the path name of the file (supports deep paths)
     * @return the file reference.
     * @deprecated use <code>javax.nio.file</code> replacement {@link #getPathFile(String)} instead
     */
    @Deprecated
    public File getFile(String name)
    {
        return getPathFile(name).toFile();
    }
    
    /**
     * Get a {@link Path} file reference for content inside of the test specific test directory.
     * <p>
     * Note: No assertions are made if the file exists or not.
     * 
     * @param name
     *            the path name of the file (supports deep paths)
     * @return the file reference.
     */
    public Path getPathFile(String name)
    {
        return dir.resolve(name);
    }

    /**
     * Ensure that the test directory is empty.
     * <p>
     * Useful for repeated testing without using the maven <code>clean</code> goal (such as within Eclipse).
     */
    public void ensureEmpty()
    {
        FS.ensureEmpty(dir);
    }

    /**
     * Get the unique testing directory while ensuring that it is empty (if not).
     * 
     * @return the unique testing directory, created, and empty.
     * @deprecated use <code>javax.nio.file</code> replacement {@link #getEmptyPathDir()} instead
     */
    @Deprecated
    public File getEmptyDir()
    {
        return getEmptyPathDir().toFile();
    }
    
    /**
     * Get the unique testing directory while ensuring that it is empty (if not).
     * 
     * @return the unique testing directory, created, and empty.
     */
    public Path getEmptyPathDir()
    {
        FS.ensureEmpty(dir);
        return dir;
    }
}
