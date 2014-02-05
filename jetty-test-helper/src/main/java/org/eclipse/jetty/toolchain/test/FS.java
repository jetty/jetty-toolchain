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
import java.io.IOException;

import org.junit.Assert;

/**
 * Common FileSystem utility methods
 */
public final class FS
{
    private FS() {
        /* prevent instantiation */
    }
    
    /**
     * Delete a file or a directory.
     * <p>
     * Note: safety mechanism only allows delete within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     * 
     * @param path
     *            the file or directory to delete.
     */
    public static void delete(File path)
    {
        if(!path.exists()) {
            return; // nothing to delete. we're done.
        }
        
        if (path.isFile())
        {
            deleteFile(path);
        }
        else if (path.isDirectory())
        {
            deleteDirectory(path);
        }
        else
        {
            Assert.fail("Not able to delete path, not a file or directory? : " + path.getAbsolutePath());
        }
    }

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
        recursiveDelete(dir);
    }

    /**
     * Delete a file.
     * <p>
     * Note: safety mechanism only allows delete file within the {@link MavenTestingUtils#getTargetTestingDir()}
     * directory.
     * 
     * @param path
     *            the path to delete.
     */
    public static void deleteFile(File path)
    {
        Assert.assertTrue("Path must be a file: " + path.getAbsolutePath(),path.isFile());
        Assert.assertTrue("Can only delete content within the /target/tests/ directory: " + path.getAbsolutePath(),FS.isTestingDir(path.getParentFile()));

        Assert.assertTrue("Failed to delete file: " + path.getAbsolutePath(),path.delete());
    }

    private static void recursiveDelete(File dir)
    {
        Assert.assertTrue("Can only delete content within the /target/tests/ directory: " + dir.getAbsolutePath(),FS.isTestingDir(dir));

        for (File file : dir.listFiles())
        {
            if (file.isDirectory())
            {
                recursiveDelete(file);
            }
            else
            {
                Assert.assertTrue("Failed to delete file: " + file.getAbsolutePath(),file.delete());
            }
        }

        Assert.assertTrue("Failed to delete dir: " + dir.getAbsolutePath(),dir.delete());
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
     */
    public static void ensureEmpty(TestingDir testingdir)
    {
        ensureEmpty(testingdir.getDir());
    }

    /**
     * Ensure the provided directory does not exist, delete it if present
     * 
     * @param dir
     *            the dir to check
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
        if (dir.exists())
        {
            Assert.assertTrue("Path exists, but should be a Dir : " + dir.getAbsolutePath(),dir.isDirectory());
        }
        else
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
     *            the dir to check
     * @return true if provided directory is a testing directory
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
        if (file.exists())
        {
            Assert.assertTrue("Updating last modified timestamp",file.setLastModified(System.currentTimeMillis()));
        }
        else
        {
            Assert.assertTrue("Creating file: " + file,file.createNewFile());
        }
    }
}
