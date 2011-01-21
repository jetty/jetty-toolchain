package org.eclipse.jetty.toolchain.test;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

public class FS
{
    /**
     * Delete a directory and all contents under it.
     * <p>
     * Note: safety mechanism only allows delete directory within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     * 
     * @param dir
     *            the directory to delete.
     */
    public static void deleteDirectory(File dir)
    {
        recursiveDelete(dir);
    }

    private static void recursiveDelete(File dir)
    {
        Assert.assertTrue("Can only delete content within the /target/tests/ directory: " + dir.getAbsolutePath(),FS.isTestingDir(dir));

        for (File file : dir.listFiles())
        {
            if (file.isFile())
            {
                Assert.assertTrue("Failed to delete file: " + file.getAbsolutePath(),file.delete());
            }
            else if (file.isDirectory())
            {
                recursiveDelete(file);
            }
        }

        Assert.assertTrue("Failed to delete dir: " + dir.getAbsolutePath(),dir.delete());
    }

    /**
     * Delete the contents of a directory and all contents under it, leaving the directory itself still in existance.
     * <p>
     * Note: safety mechanism only allows clean directory within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
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
    public static void ensureEmpty(File dir)
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
    public static void ensureEmpty(TestingDir testingdir)
    {
        ensureEmpty(testingdir.getDir());
    }

    /**
     * Ensure the provided directory does not exist, delete it if present
     * 
     * @param dir
     * @throws IOException
     */
    public static void ensureDeleted(File dir)
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

    /**
     * Internal class used to detect if the directory is a valid testing directory.
     * <p>
     * Used as part of the validation on what directories are safe to delete from.
     * 
     * @param dir
     * @return
     */
    protected static boolean isTestingDir(File dir)
    {
        return dir.getAbsolutePath().startsWith(MavenTestingUtils.getTargetTestingDir().getAbsolutePath());
    }

    /**
     * Create an empty file at the location. If the file exists, just update the last modified timestamp.
     * 
     * @param file
     *            the file to create or update the timestamp of.
     * @throws IOException
     */
    public static void touch(File file) throws IOException
    {
        if (!file.exists())
        {
            Assert.assertTrue("Creating file: " + file,file.createNewFile());
        }
        else
        {
            Assert.assertTrue("Updating last modified timestamp",file.setLastModified(System.currentTimeMillis()));
        }
    }
}
