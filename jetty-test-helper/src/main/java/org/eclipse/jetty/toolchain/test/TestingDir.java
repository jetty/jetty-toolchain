package org.eclipse.jetty.toolchain.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.MethodRule;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A junit 4.x {@link Rule} to provide a common, easy to use, testing directory that is unique per unit test.
 * <p>
 * Similar in scope to the {@link TemporaryFolder} rule, as it creates a directory, when asked (via {@link #getDir()}
 * or {@link #getEmptyDir()}) in the maven project familiar and friendly location:
 * <code>${basedir}/target/tests/${testclass}/${testmethod}</code>.
 * <p>
 * Note: existing facilities within {@link MavenTestingUtils} for keeping the directory name short for the sake of
 * windows users is being used.
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

    public File getDir()
    {
        if (dir.exists())
        {
            return dir;
        }

        Assert.assertTrue("Creating testing dir",dir.mkdirs());
        return dir;
    }

    public File getFile(String name)
    {
        return new File(dir,OS.separators(name));
    }

    public void ensureEmpty() throws IOException
    {
        FS.ensureEmpty(dir);
    }

    public File getEmptyDir() throws IOException
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
