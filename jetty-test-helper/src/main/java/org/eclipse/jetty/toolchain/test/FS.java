//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.toolchain.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Common FileSystem utility methods
 */
public final class FS
{
    private FS()
    {
        /* prevent instantiation */
    }

    /**
     * Delete a file or a directory.
     * <p>
     * Note: safety mechanism only allows delete within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param path the file or directory to delete.
     */
    public static void delete(Path path)
    {
        if (!Files.exists(path))
        {
            return; // nothing to delete. we're done.
        }

        if (Files.isRegularFile(path))
        {
            deleteFile(path);
        }
        else if (Files.isDirectory(path))
        {
            deleteDirectory(path);
        }
        else
        {
            fail("Not able to delete path, not a file or directory? : " + path.toAbsolutePath());
        }
    }

    /**
     * Delete a file or a directory.
     * <p>
     * Note: safety mechanism only allows delete within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param path the file or directory to delete.
     */
    public static void delete(File path)
    {
        delete(path.toPath());
    }

    /**
     * Delete a directory and all contents under it.
     * <p>
     * Note: safety mechanism only allows delete directory within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param dir the directory to delete.
     */
    public static void deleteDirectory(File dir)
    {
        deleteDirectory(dir.toPath());
    }

    /**
     * Delete a directory and all contents under it.
     * <p>
     * Note: safety mechanism only allows delete directory within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param dir the directory to delete.
     */
    public static void deleteDirectory(Path dir)
    {
        recursiveDeleteDir(dir);
    }

    /**
     * Delete a file.
     * <p>
     * Note: safety mechanism only allows delete file within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param path the path to delete.
     */
    public static void deleteFile(File path)
    {
        assertTrue(path.isFile(), "Path must be a file: " + path.getAbsolutePath());
        assertTrue(FS.isTestingDir(path.getParentFile()), "Can only delete content within the /target/tests/ directory: " + path.getAbsolutePath());

        assertTrue(path.delete(), "Failed to delete file: " + path.getAbsolutePath());
    }

    /**
     * Delete a file.
     * <p>
     * Note: safety mechanism only allows delete file within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param path the path to delete.
     */
    public static void deleteFile(Path path)
    {
        String location = path.toAbsolutePath().toString();

        if (Files.exists(path, LinkOption.NOFOLLOW_LINKS))
        {
            assertTrue(Files.isRegularFile(path) || Files.isSymbolicLink(path), "Path must be a file or link: " + location);
            assertTrue(FS.isTestingDir(path.getParent()), "Can only delete content within the /target/tests/ directory: " + location);
            try
            {
                assertTrue(Files.deleteIfExists(path), "Failed to delete file: " + location);
            }
            catch (IOException e)
            {
                fail("Unable to delete file: " + location, e);
            }
        }
    }

    /**
     * Delete a directory. (only if it is empty)
     * <p>
     * Note: safety mechanism only allows delete file within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param path the path to delete.
     */
    public static void deleteDir(Path path)
    {
        String location = path.toAbsolutePath().toString();

        if (Files.exists(path))
        {
            assertTrue(Files.isDirectory(path), "Path must be a file: " + location);
            assertTrue(FS.isTestingDir(path.getParent()), "Can only delete content within the /target/tests/ directory: " + location);
            try
            {
                assertTrue(Files.deleteIfExists(path), "Failed to delete directory: " + location);
            }
            catch (IOException e)
            {
                fail("Unable to delete directory: " + location, e);
            }
        }
    }

    private static void recursiveDeleteDir(Path path)
    {
        String location = path.toAbsolutePath().toString();
        assertTrue(FS.isTestingDir(path), "Can only delete content within the /target/tests/ directory: " + location);

        // Get entries in this path
        try (DirectoryStream<Path> dir = Files.newDirectoryStream(path))
        {
            for (Path entry : dir)
            {
                if (Files.isDirectory(entry))
                {
                    recursiveDeleteDir(entry);
                }
                else
                {
                    deleteFile(entry);
                }
            }
        }
        catch (DirectoryIteratorException e)
        {
            fail("Unable to (recursively) delete path: " + location, e);
        }
        catch (IOException e)
        {
            fail("Unable to (recursively) delete path: " + location, e);
        }

        // delete itself
        deleteDir(path);
    }

    /**
     * Delete the contents of a directory and all contents under it, leaving the directory itself still in existance.
     * <p>
     * Note: safety mechanism only allows clean directory within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param dir the directory to delete.
     */
    public static void cleanDirectory(File dir)
    {
        cleanDirectory(dir.toPath());
    }

    /**
     * Delete the contents of a directory and all contents under it, leaving the directory itself still in existance.
     * <p>
     * Note: safety mechanism only allows clean directory within the {@link MavenTestingUtils#getTargetTestingDir()} directory.
     *
     * @param dir the directory to delete.
     */
    public static void cleanDirectory(Path dir)
    {
        deleteDirectory(dir);
        ensureDirExists(dir);
    }

    /**
     * Ensure the provided directory exists, and contains no content (empty)
     *
     * @param dir the dir to check.
     */
    public static void ensureEmpty(File dir)
    {
        ensureEmpty(dir.toPath());
    }

    /**
     * Ensure the provided directory exists, and contains no content (empty)
     *
     * @param dir the dir to check.
     */
    public static void ensureEmpty(Path dir)
    {
        if (Files.exists(dir))
        {
            FS.cleanDirectory(dir);
        }
        else
        {
            FS.ensureDirExists(dir);
        }
    }

    /**
     * Ensure the provided directory does not exist, delete it if present
     *
     * @param dir the dir to check
     */
    public static void ensureDeleted(File dir)
    {
        ensureDeleted(dir.toPath());
    }

    /**
     * Ensure the provided directory does not exist, delete it if present
     *
     * @param dir the dir to check
     */
    public static void ensureDeleted(Path dir)
    {
        if (Files.exists(dir))
        {
            FS.deleteDirectory(dir);
        }
    }

    /**
     * Ensure that directory exists, create it if not present. Leave it alone if already there.
     *
     * @param dir the dir to check.
     */
    public static void ensureDirExists(File dir)
    {
        if (dir.exists())
        {
            assertTrue(dir.isDirectory(), "Path exists, but should be a Dir : " + dir.getAbsolutePath());
        }
        else
        {
            assertTrue(dir.mkdirs(), "Creating dir: " + dir);
        }
    }

    /**
     * Ensure that directory exists, create it if not present. Leave it alone if already there.
     *
     * @param dir the dir to check.
     */
    public static void ensureDirExists(Path dir)
    {
        if (Files.exists(dir))
        {
            assertTrue(Files.isDirectory(dir), "Path exists, but should be a Dir : " + dir.toAbsolutePath());
        }
        else
        {
            try
            {
                Files.createDirectories(dir);
                assertTrue(Files.exists(dir), "Failed to create dir: " + dir);
            }
            catch (IOException e)
            {
                fail("Failed to create directory: " + dir, e);
            }
        }
    }

    /**
     * Internal class used to detect if the directory is a valid testing directory.
     * <p>
     * Used as part of the validation on what directories are safe to delete from.
     *
     * @param dir the dir to check
     * @return true if provided directory is a testing directory
     */
    protected static boolean isTestingDir(File dir)
    {
        return isTestingDir(dir.toPath());
    }

    /**
     * Internal class used to detect if the directory is a valid testing directory.
     * <p>
     * Used as part of the validation on what directories are safe to delete from.
     *
     * @param dir the dir to check
     * @return true if provided directory is a testing directory
     */
    protected static boolean isTestingDir(Path dir)
    {
        try
        {
            return dir.toRealPath().startsWith(MavenTestingUtils.getTargetTestingPath());
        }
        catch (IOException e)
        {
            // Fallback when toRealPath() fails (on some filesystems)
            return dir.toAbsolutePath().startsWith(MavenTestingUtils.getTargetTestingPath());
        }
    }

    /**
     * Create an empty file at the location. If the file exists, just update the last modified timestamp.
     *
     * @param file the file to create or update the timestamp of.
     * @throws IOException if unable to create the new file.
     */
    public static void touch(File file) throws IOException
    {
        if (file.exists())
        {
            assertTrue(file.setLastModified(System.currentTimeMillis()), "Updating last modified timestamp");
        }
        else
        {
            assertTrue(file.createNewFile(), "Creating file: " + file);
        }
    }

    /**
     * Create an empty file at the location. If the file exists, just update the last modified timestamp.
     *
     * @param file the file to create or update the timestamp of.
     * @throws IOException if unable to create the new file.
     */
    public static void touch(Path file) throws IOException
    {
        if (Files.exists(file))
        {
            FileTime timeOrig = Files.getLastModifiedTime(file);
            Files.setLastModifiedTime(file, FileTime.from(System.currentTimeMillis(), TimeUnit.MILLISECONDS));
            FileTime timeNow = Files.getLastModifiedTime(file);
            // Verify that timestamp was actually updated.
            assertThat("Timestamp updated", timeOrig, not(equalTo(timeNow)));
        }
        else
        {
            Files.createFile(file);
            assertTrue(Files.exists(file), "Created new file?: " + file);
        }
    }

    /**
     * Convert path separators to the System path separators.
     * <p>
     * This helps ensure that the paths provided in the unit tests work equally as well on unix / osx / windows.
     *
     * @param path the raw path to convert
     * @return the converted path
     */
    public static String separators(String path)
    {
        StringBuilder ret = new StringBuilder();
        for (char c : path.toCharArray())
        {
            if ((c == '/') || (c == '\\'))
            {
                ret.append(File.separatorChar);
            }
            else
            {
                ret.append(c);
            }
        }
        return ret.toString();
    }
}
