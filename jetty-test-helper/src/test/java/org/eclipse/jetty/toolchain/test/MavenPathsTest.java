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

import org.junit.jupiter.api.Test;

import static org.eclipse.jetty.toolchain.test.PathMatchers.isDirectory;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isRegularFile;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MavenPathsTest
{
    @Test
    public void testTargetDir()
    {
        Path dir = MavenPaths.targetDir();
        assertEquals("target", dir.getFileName().toString());
    }

    @Test
    public void testGetTargetFileBasic() throws IOException
    {
        Path dir = MavenPaths.targetDir();

        // File in .../target/pizza.log
        Path expected = dir.resolve("pizza.log");
        FS.touch(expected);

        Path actual = MavenPaths.targetDir().resolve("pizza.log");
        assertThat(actual, isSame(expected));
    }

    @Test
    public void testFindTestResourceDir_InSrc()
    {
        Path dir = MavenPaths.findTestResourceDir("breakfast");
        assertThat("Test resource dir", dir, isDirectory());
    }

    @Test
    public void testFindTestResourceDir_InTarget() throws IOException
    {
        Path testDir = MavenPaths.targetDir().resolve("test-classes/meal/lunch");
        if (!Files.exists(testDir))
            Files.createDirectories(testDir);

        Path dir = MavenPaths.findTestResourceDir("meal/lunch");
        assertThat("Test resource dir", dir, isDirectory());
    }

    @Test
    public void testFindTestResourceFile_InSrc()
    {
        Path file = MavenPaths.findTestResourceFile("breakfast/eggs.txt");
        assertThat("Test resource file", file, isRegularFile());
    }

    @Test
    public void testFindTestResourceFile_InTarget() throws IOException
    {
        Path testFile = MavenPaths.targetDir().resolve("test-classes/meal/dinner/menu.txt");
        if (!Files.exists(testFile.getParent()))
            Files.createDirectories(testFile.getParent());
        FS.touch(testFile);

        Path file = MavenPaths.findTestResourceFile("meal/dinner/menu.txt");
        assertThat("Test resource file", file, isRegularFile());
    }

    @Test
    public void testFindMainResourceDir_InSrc()
    {
        Path dir = MavenPaths.findMainResourceDir("META-INF/services");
        assertThat("Main resource dir", dir, isDirectory());
    }

    @Test
    public void testFindMainResourceDir_InTarget() throws IOException
    {
        Path testDir = MavenPaths.targetDir().resolve("classes/META-INF/ignored");
        if (!Files.exists(testDir))
            Files.createDirectories(testDir);

        Path dir = MavenPaths.findMainResourceDir("META-INF/ignored");
        assertThat("Main resource dir", dir, isDirectory());
    }

    @Test
    public void testFindMainResourceFile_InSrc()
    {
        Path file = MavenPaths.findMainResourceFile("META-INF/services/org.junit.jupiter.api.extension.Extension");
        assertThat("Main resource file", file, isRegularFile());
    }

    @Test
    public void testFindMainResourceFile_InTarget() throws IOException
    {
        Path testFile = MavenPaths.targetDir().resolve("classes/META-INF/ignored/info.txt");
        if (!Files.exists(testFile.getParent()))
            Files.createDirectories(testFile.getParent());
        FS.touch(testFile);

        Path file = MavenPaths.findMainResourceFile("META-INF/ignored/info.txt");
        assertThat("Main resource file", file, isRegularFile());
    }
}
