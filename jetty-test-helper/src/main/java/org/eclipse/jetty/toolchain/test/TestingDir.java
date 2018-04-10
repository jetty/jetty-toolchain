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

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * A junit 4.x {@link org.junit.Rule} to provide a common, easy to use, testing directory that is unique per unit test method.
 * <p>
 * Similar in scope to the {@link org.junit.rules.TemporaryFolder} rule, as it creates a directory, when asked (via {@link #getPath()} or {@link #getEmptyPathDir()}) in the maven
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
 *     public void testUseDirectory() throws IOException
 *     {
 *         Path appDir = testingdir.getPathFile("app");
 *         Path tmpDir = testingdir.getPathFile("tmp");
 *
 *         FS.ensureEmpty(appDir);
 *         FS.ensureEmpty(tmpDir);
 *
 *         Path index = appDir.resolve("index.html");
 *         try(BufferedWriter writer = Files.newBufferedWriter(index, StandardCharsets.UTF_8))
 *         {
 *             writer.write("Hello World");
 *         }
 *
 *         Server server = new Server();
 *         server.setTmpDir(tmpDir);
 *         server.addWebApp(appDir);
 *         server.start();
 *
 *         Client client = new Client();
 *         String response = client.request("http://localhost/app/");
 *
 *         Assert.assertThat(response, containsString("Hello World"));
 *     }
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
     */
    public Path getEmptyPathDir()
    {
        FS.ensureEmpty(dir);
        return dir;
    }
}
