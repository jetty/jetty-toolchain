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

import org.junit.Assert;

/**
 * Assertions of various FileSytem Paths
 */
public final class PathAssert
{
    private PathAssert() {
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
        assertExists(msg,path);
        Assert.assertTrue(msg + " path should be a Dir : " + path.getAbsolutePath(),path.isDirectory());
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
        assertExists(msg,path);
        Assert.assertTrue(msg + " path should be a File : " + path.getAbsolutePath(),path.isFile());
    }

    /**
     * Assert that the path exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should exist
     */
    public static void assertExists(String msg, File path)
    {
        Assert.assertTrue(msg + " path should exist: " + path.getAbsolutePath(),path.exists());
    }

    /**
     * Assert that the path does not exist.
     * 
     * @param msg
     *            message about the test (used in case of assertion failure)
     * @param path
     *            the path that should not exist
     */
    public static void assertNotExists(String msg, File path)
    {
        Assert.assertFalse(msg + " path should not exist: " + path.getAbsolutePath(),path.exists());
    }
}
