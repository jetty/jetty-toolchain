package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.Matchers.startsWith;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

public class FS
{
    /**
     * Delete a directory and all contents under it.
     * <p>
     * Note: safety mechanism only allows delete directory within the {@link MavenTestingUtils#getTargetTestingDir()}
     * directory.
     * 
     * @param dir
     *            the directory to delete.
     */
    public static void deleteDirectory(File dir)
    {
        File targetDir = MavenTestingUtils.getTargetTestingDir();
        Assert.assertThat("Can only delete content within the /target/tests/ directory",dir.getAbsolutePath(),startsWith(targetDir.getAbsolutePath()));
    }

    /**
     * Delete the contents of a directory and all contents under it, leaving the directory itself still in existance.
     * <p>
     * Note: safety mechanism only allows clean directory within the {@link MavenTestingUtils#getTargetTestingDir()}
     * directory.
     * 
     * @param dir
     *            the directory to delete.
     */
    public static void cleanDirectory(File dir)
    {
        deleteDirectory(dir);
        ensureDirExists(dir);
    }

    /**
     * Ensure the provided directory exists, and contains no content (empty)
     * 
     * @param dir
     *            the dir to check.
     * @throws IOException
     */
    public static void ensureEmpty(File dir) throws IOException
    {
        if (dir.exists())
        {
            FS.cleanDirectory(dir);
        }
        else
        {
            Assert.assertTrue("Creating dir: " + dir,dir.mkdirs());
        }
    }

    /**
     * Ensure the provided directory exists, and contains no content (empty)
     * 
     * @param testingdir
     *            the dir to check.
     * @throws IOException
     */
    public static void ensureEmpty(TestingDir testingdir) throws IOException
    {
        ensureEmpty(testingdir.getDir());
    }

    /**
     * Ensure the provided directory does not exist, delete it if present
     * 
     * @param dir
     * @throws IOException
     */
    public static void ensureDeleted(File dir) throws IOException
    {
        if (dir.exists())
        {
            FS.deleteDirectory(dir);
        }
    }

    /**
     * Ensure that directory exists, create it if not present. Leave it alone if already there.
     * 
     * @param dir
     *            the dir to check.
     */
    public static void ensureDirExists(File dir)
    {
        if (!dir.exists())
        {
            Assert.assertTrue("Creating dir: " + dir,dir.mkdirs());
        }
    }
}
