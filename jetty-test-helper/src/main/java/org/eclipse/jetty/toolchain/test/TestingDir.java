//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
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

import org.junit.Assert;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A junit 4.x {@link Rule} to provide a common, easy to use, testing directory that is unique per unit test method.
 * <p>
 * Similar in scope to the {@link TemporaryFolder} rule, as it creates a directory, when asked (via {@link #getDir()} or {@link #getEmptyDir()}) in the maven
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
    private File dir;

    public Statement apply(final Statement statement, final Description description)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                dir = MavenTestingUtils.getTargetTestingDir(description.getTestClass(),description.getMethodName());
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
    public File getDir()
    {
        if (dir.exists())
        {
            return dir;
        }

        Assert.assertTrue("Creating testing dir",dir.mkdirs());
        return dir;
    }

    /**
     * Get a file inside of the test specific test directory.
     * <p>
     * Note: No assertions are made if the file exists or not.
     * 
     * @param name
     *            the path name of the file (supports deep paths)
     * @return the file reference.
     */
    public File getFile(String name)
    {
        return new File(dir,OS.separators(name));
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
     */
    public File getEmptyDir()
    {
        if (dir.exists())
        {
            FS.ensureEmpty(dir);
            return dir;
        }

        Assert.assertTrue("Creating testing dir",dir.mkdirs());
        return dir;
    }
}
