package org.eclipse.jetty.toolchain.test;

import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A junit 4.x {@link Rule} to provide a common, easy to use, testing directory that is unique per unit test.
 * <p>
 * Similar in scope to the {@link TemporaryFolder} rule, as it creates a directory, when asked (via {@link #getDir()} or {@link #getEmptyDir()}) in the maven
 * project familiar and friendly location: <code>${basedir}/target/tests/${testclass}/${testmethod}</code>.
 * <p>
 * Note: existing facilities within {@link MavenTestingUtils} for keeping the directory name short for the sake of windows users is being used.
 */
public class TestingDir implements MethodRule
{
    private File dir;

    public Statement apply(final Statement statement, final FrameworkMethod method, final Object target)
    {
        return new Statement()
        {
            @Override
            public void evaluate() throws Throwable
            {
                dir = MavenTestingUtils.getTargetTestingDir(target.getClass(),method.getName());
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
