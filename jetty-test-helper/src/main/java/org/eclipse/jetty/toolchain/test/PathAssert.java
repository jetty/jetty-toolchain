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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

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
        assertTrue(path.isDirectory(),msg + " path should be a Dir : " + path.getAbsolutePath());
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
        assertTrue(Files.isDirectory(path),msg + " path should be a Dir : " + path);
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
        assertTrue(path.isFile(),msg + " path should be a File : " + path.getAbsolutePath());
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
        assertTrue(Files.isRegularFile(path),msg + " path should be a File : " + path);
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
        assertTrue(path.exists(),msg + " path should exist: " + path.getAbsolutePath());
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
        assertTrue(Files.exists(path),msg + " path should exist: " + path);
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
        assertFalse(path.exists(),msg + " path should not exist: " + path.getAbsolutePath());
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
        assertFalse(Files.exists(path),msg + " path should not exist: " + path);
    }
}
