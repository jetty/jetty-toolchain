//
//  ========================================================================
//  Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
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

import org.junit.Assert;

/**
 * Assertions of various FileSytem Paths
 */
public final class PathAssert
{
    private PathAssert()
    {
        /* prevent instantiation */
    }

    /**
     * Assert that the Directory exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist, and be a directory
     */
    public static void assertDirExists(String msg, File path)
    {
        assertPathExists(msg,path);
        Assert.assertTrue(msg + " path should be a Dir : " + path.getAbsolutePath(),path.isDirectory());
    }

    /**
     * Assert that the Directory path exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist, and be a directory
     */
    public static void assertDirExists(String msg, Path path)
    {
        assertPathExists(msg,path);
        Assert.assertTrue(msg + " path should be a Dir : " + path,Files.isDirectory(path));
    }

    /**
     * Assert that the File exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist, and be a file
     */
    public static void assertFileExists(String msg, File path)
    {
        assertPathExists(msg,path);
        Assert.assertTrue(msg + " path should be a File : " + path.getAbsolutePath(),path.isFile());
    }

    /**
     * Assert that the File exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist, and be a file
     */
    public static void assertFileExists(String msg, Path path)
    {
        assertPathExists(msg,path);
        Assert.assertTrue(msg + " path should be a File : " + path,Files.isRegularFile(path));
    }

    /**
     * Assert that the path exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist
     */
    public static void assertPathExists(String msg, File path)
    {
        Assert.assertTrue(msg + " path should exist: " + path.getAbsolutePath(),path.exists());
    }

    /**
     * Assert that the path exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist
     */
    public static void assertPathExists(String msg, Path path)
    {
        Assert.assertTrue(msg + " path should exist: " + path,Files.exists(path));
    }

    /**
     * Assert that the path does not exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should not exist
     */
    public static void assertNotPathExists(String msg, File path)
    {
        Assert.assertFalse(msg + " path should not exist: " + path.getAbsolutePath(),path.exists());
    }

    /**
     * Assert that the path does not exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should not exist
     */
    public static void assertNotPathExists(String msg, Path path)
    {
        Assert.assertFalse(msg + " path should not exist: " + path,Files.exists(path));
    }
}
