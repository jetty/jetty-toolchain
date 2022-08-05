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
import java.nio.file.Path;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Assertions of various FileSytem Paths
 * @deprecated use {@link PathMatchers} with {@code assertThat()}
 */
@Deprecated(forRemoval = true, since = "6.0")
public final class PathAssert
{
    private PathAssert()
    {
        /* prevent instantiation */
    }

    /**
     * Assert that the Directory exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should exist, and be a directory
     * @deprecated use {@code assertThat(msg, path.toPath(), PathMatchers.isDirectory());} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertDirExists(String msg, File path)
    {
        assertThat(msg, path.toPath(), PathMatchers.isDirectory());
    }

    /**
     * Assert that the Directory path exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should exist, and be a directory
     * @deprecated use {@code assertThat(msg, path, PathMatchers.isDirectory());} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertDirExists(String msg, Path path)
    {
        assertThat(msg, path, PathMatchers.isDirectory());
    }

    /**
     * Assert that the File exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should exist, and be a file
     * @deprecated use {@code assertThat(msg, path.toPath(), PathMatchers.isRegularFile());} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertFileExists(String msg, File path)
    {
        assertThat(msg, path.toPath(), PathMatchers.isRegularFile());
    }

    /**
     * Assert that the File exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should exist, and be a file
     * @deprecated use {@code assertThat(msg, path, PathMatchers.isRegularFile());} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertFileExists(String msg, Path path)
    {
        assertThat(msg, path, PathMatchers.isRegularFile());
    }

    /**
     * Assert that the path exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should exist
     * @deprecated use {@code assertThat(msg, path.toPath(), PathMatchers.exists());} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertPathExists(String msg, File path)
    {
        assertThat(msg, path.toPath(), PathMatchers.exists());
    }

    /**
     * Assert that the path exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should exist
     * @deprecated use {@code assertThat(msg, path, PathMatchers.exists());} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertPathExists(String msg, Path path)
    {
        assertThat(msg, path, PathMatchers.exists());
    }

    /**
     * Assert that the path does not exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should not exist
     * @deprecated use {@code assertThat(msg, path.toPath(), not(PathMatchers.exists()));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertNotPathExists(String msg, File path)
    {
        assertThat(msg, path.toPath(), not(PathMatchers.exists()));
    }

    /**
     * Assert that the path does not exist.
     *
     * @param msg message about the test (used in case of assertion failure)
     * @param path the path that should not exist
     * @deprecated use {@code assertThat(msg, path, not(PathMatchers.exists()));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertNotPathExists(String msg, Path path)
    {
        assertThat(msg, path, not(PathMatchers.exists()));
    }
}
