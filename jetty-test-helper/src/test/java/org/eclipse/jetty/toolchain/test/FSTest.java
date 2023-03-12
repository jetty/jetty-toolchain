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
import java.nio.file.FileSystemException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

@SuppressWarnings("javadoc")
public class FSTest
{
    private void assertInvalidTestingDir(File dir)
    {
        assertFalse(FS.isTestingDir(dir),"Should be an invalid testing directory: " + dir.getAbsolutePath());
    }

    private void assertValidTestingDir(File dir)
    {
        assertTrue(FS.isTestingDir(dir),"Should be a valid testing directory: " + dir.getAbsolutePath());
    }

    @Test
    public void testIsTestingDirInvalid()
    {
        assertInvalidTestingDir(MavenTestingUtils.getBaseDir());

        String tmpdir = System.getProperty("java.io.tmpdir");
        assertInvalidTestingDir(new File(tmpdir));

        String userdir = System.getProperty("user.dir");
        assertInvalidTestingDir(new File(userdir));

        String homedir = System.getProperty("user.home");
        assertInvalidTestingDir(new File(homedir));
    }

    @Test
    public void testIsTestingDirValid()
    {
        File testingDir = MavenTestingUtils.getTargetTestingDir();
        assertThat("Ensuring that our expectations are sane",testingDir.getAbsolutePath(),endsWith(FS.separators("target/tests")));

        assertValidTestingDir(testingDir);
        assertValidTestingDir(new File(testingDir,"foo"));
        assertValidTestingDir(new File(testingDir,FS.separators("oejt.Dummy/lib/jsp")));
    }

    @Test
    public void testDeleteDirectory() throws IOException
    {
        File testdir = MavenTestingUtils.getTargetTestingDir("testDeleteDirectory");

        FS.ensureDirExists(testdir);

        createDummyDirectoryContent(testdir);

        PathAssert.assertDirExists("pre-delete",testdir);
        FS.deleteDirectory(testdir);
        PathAssert.assertNotPathExists("post-delete",testdir);
    }

    private void createDummyDirectoryContent(File testdir) throws IOException
    {
        // Create 10 files in root dir.
        File f;
        for (int i = 0; i < 10; i++)
        {
            f = new File(testdir,"dummy-" + i);
            FS.touch(f);
        }

        File d = new File(testdir,"subdir");
        FS.ensureDirExists(d);

        try
        {
            Files.createSymbolicLink(d.toPath().resolve("brokenlink"), d.toPath().resolve("doesNotExist"));
        }
        catch (FileSystemException ignore)
        {
            // Some OS's (Windows) cannot create symlinks, so we ignore this failure
        }
        
        for (int i = 0; i < 10; i++)
        {
            f = new File(d,"subdummy-" + i);
            FS.touch(f);
        }
    }

    @Test
    @EnabledOnOs(WINDOWS)
    public void testSeparatorsWindows()
    {
        assertEquals("target\\tests\\tests-Foo", FS.separators("target/tests/tests-Foo"));
        assertEquals("target\\tests\\tests-Foo", FS.separators("target/tests\\tests-Foo"));
    }

    @Test
    @EnabledOnOs({LINUX, MAC})
    public void testSeparatorsUnix()
    {
        assertEquals("target/tests/tests-Foo", FS.separators("target/tests/tests-Foo"));
        assertEquals("target/tests/tests-Foo", FS.separators("target/tests\\tests-Foo"));
    }
}
