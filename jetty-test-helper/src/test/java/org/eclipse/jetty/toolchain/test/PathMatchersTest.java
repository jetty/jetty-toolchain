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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.jupiter.WorkDir;
import org.eclipse.jetty.toolchain.test.jupiter.WorkDirExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.eclipse.jetty.toolchain.test.PathMatchers.exists;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isDirectory;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isEmptyDirectory;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isRegularFile;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ExtendWith(WorkDirExtension.class)
public class PathMatchersTest
{
    @Test
    public void testExists(WorkDir workDir) throws IOException
    {
        Path path = workDir.getEmptyPathDir().resolve("test.txt");
        assertThat(path, not(exists()));

        FS.touch(path);
        assertThat(path, exists());
    }

    @Test
    public void testIsRegularFile(WorkDir workDir) throws IOException
    {
        Path path = workDir.getEmptyPathDir().resolve("test.txt");
        assertThat(path, not(isRegularFile()));

        FS.touch(path);
        assertThat(path, isRegularFile());

        Path dir = workDir.getPath().resolve("foo-dir");
        assertThat("Doesn't exist", dir, not(isRegularFile()));

        Files.createDirectory(dir);
        assertThat("Not a file", dir, not(isRegularFile()));
    }

    @Test
    public void testIsDirectory(WorkDir workDir) throws IOException
    {
        Path path = workDir.getEmptyPathDir().resolve("test.txt");
        assertThat("Does not exist", path, not(isDirectory()));

        FS.touch(path);
        assertThat("Not a directory", path, not(isDirectory()));

        Path dir = workDir.getPath().resolve("foo-dir");
        assertThat("Does not exist", dir, not(isDirectory()));

        Files.createDirectory(dir);
        assertThat("Is a directory", dir, isDirectory());
    }

    @Test
    public void testIsEmptyDirectory(WorkDir workDir) throws IOException
    {
        Path path = workDir.getEmptyPathDir().resolve("test.txt");
        assertThat("Does not exist", path, not(isEmptyDirectory()));

        FS.touch(path);
        assertThat("Not a directory", path, not(isEmptyDirectory()));

        Path dir = workDir.getPath().resolve("foo-dir");
        assertThat("Does not exist", dir, not(isEmptyDirectory()));

        Files.createDirectory(dir);
        assertThat("Is an empty directory", dir, isEmptyDirectory());

        Path content = dir.resolve("foo.dat");
        FS.touch(content);
        assertThat("Is an empty directory", dir, not(isEmptyDirectory()));
    }

    @Test
    public void testIsSame(WorkDir workDir) throws IOException
    {
        Path pathA = workDir.getEmptyPathDir().resolve("test.txt");
        FS.touch(pathA);
        Path dir = workDir.getPath().resolve("dir");
        Files.createDirectory(dir);
        Path relativeRef = workDir.getPath().resolve("dir/../test.txt");
        assertThat("toString should not match", pathA.toString(), not(is(relativeRef.toString())));

        assertThat("Is the same file", relativeRef, isSame(pathA));
    }
}
