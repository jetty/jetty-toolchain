package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.CoreMatchers.*;
import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class FSTest
{
    private void assertInvalidTestingDir(File dir)
    {
        Assert.assertFalse("Should be an invalid testing directory: " + dir.getAbsolutePath(),FS.isTestingDir(dir));
    }

    private void assertValidTestingDir(File dir)
    {
        Assert.assertTrue("Should be a valid testing directory: " + dir.getAbsolutePath(),FS.isTestingDir(dir));
    }

    @Test
    public void testIsTestingDirInvalid()
    {
        assertInvalidTestingDir(MavenTestingUtils.getBasedir());

        String tmpdir = System.getProperty("java.io.tmpdir");
        assertInvalidTestingDir(new File(tmpdir));

        String userdir = System.getProperty("user.dir");
        assertInvalidTestingDir(new File(userdir));

        String homedir = System.getProperty("user.home");
        assertInvalidTestingDir(new File(homedir));
    }

    @Test
    public void testIsTestingDirValid()
    {
        File testingDir = MavenTestingUtils.getTargetTestingDir();
        Assert.assertThat("Ensuring that our expectations are sane",testingDir.getAbsolutePath(),endsWith(OS.separators("target/tests")));

        assertValidTestingDir(testingDir);
        assertValidTestingDir(new File(testingDir,"foo"));
        assertValidTestingDir(new File(testingDir,OS.separators("oejt.Dummy/lib/jsp")));
    }

    @Test
    public void testDeleteDirectory() throws IOException
    {
        File testdir = MavenTestingUtils.getTargetTestingDir("testDeleteDirectory");

        FS.ensureDirExists(testdir);

        createDummyDirectoryContent(testdir);

        PathAssert.assertDirExists("pre-delete",testdir);
        FS.deleteDirectory(testdir);
        PathAssert.assertNotExists("post-delete",testdir);
    }

    private void createDummyDirectoryContent(File testdir) throws IOException
    {
        // Create 10 files in root dir.
        File f;
        for (int i = 0; i < 10; i++)
        {
            f = new File(testdir,"dummy-" + i);
            FS.touch(f);
        }

        File d = new File(testdir,"subdir");
        FS.ensureDirExists(d);

        for (int i = 0; i < 10; i++)
        {
            f = new File(d,"subdummy-" + i);
            FS.touch(f);
        }
    }
}
