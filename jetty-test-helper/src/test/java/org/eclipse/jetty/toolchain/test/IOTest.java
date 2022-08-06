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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.jupiter.WorkDir;
import org.junit.jupiter.api.Test;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class IOTest
{
    @Test
    public void testCopyReaderWriter(WorkDir workDir) throws IOException
    {
        Path root = workDir.getEmptyPathDir();
        Path testInput = root.resolve("test.dat");
        String content = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".repeat(100_000);
        Files.writeString(testInput, content, UTF_8);

        Path testOutput = root.resolve("output.dat");
        try(BufferedReader reader = Files.newBufferedReader(testInput, UTF_8);
            BufferedWriter writer = Files.newBufferedWriter(testOutput, UTF_8))
        {
            IO.copy(reader, writer);
        }

        String actual = Files.readString(testOutput, UTF_8);
        assertThat(actual, is(content));
    }

    @Test
    public void testCopyInputStreamOutputStream(WorkDir workDir) throws IOException
    {
        Path root = workDir.getEmptyPathDir();
        Path testInput = root.resolve("test.dat");
        String content = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".repeat(100_000);
        Files.writeString(testInput, content, UTF_8);

        Path testOutput = root.resolve("output.dat");
        try(InputStream in = Files.newInputStream(testInput);
            OutputStream out = Files.newOutputStream(testOutput))
        {
            IO.copy(in, out);
        }

        String actual = Files.readString(testOutput, UTF_8);
        assertThat(actual, is(content));
    }

    @Test
    public void testCopy_FileFile(WorkDir workDir) throws IOException
    {
        Path root = workDir.getEmptyPathDir();
        Path testInput = root.resolve("test.dat");
        String content = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".repeat(100_000);
        Files.writeString(testInput, content, UTF_8);

        Path testOutput = root.resolve("output.dat");
        IO.copy(testInput, testOutput);

        String actual = Files.readString(testOutput, UTF_8);
        assertThat(actual, is(content));
    }

    @Test
    public void testCopy_DirDir(WorkDir workDir) throws IOException
    {
        Path root = workDir.getEmptyPathDir();

        Path inDir = root.resolve("in");
        Files.createDirectory(inDir);
        Files.writeString(inDir.resolve("data.dat"), "Contents of data.dat");
        Files.writeString(inDir.resolve("foo.txt"), "Contents of foo.txt");
        Files.createDirectory(inDir.resolve("more"));
        Files.writeString(inDir.resolve("more/bar.dat"), "Contents of bar.dat");
        Files.writeString(inDir.resolve("more/zed.txt"), "Contents of zed.txt");

        Path outDir = root.resolve("out");
        Files.createDirectory(outDir);
        IO.copy(inDir, outDir);

        // Test output directory
        assertThat(Files.readString(outDir.resolve("data.dat")), is("Contents of data.dat"));
        assertThat(Files.readString(outDir.resolve("foo.txt")), is("Contents of foo.txt"));
        assertThat(Files.readString(outDir.resolve("more/bar.dat")), is("Contents of bar.dat"));
        assertThat(Files.readString(outDir.resolve("more/zed.txt")), is("Contents of zed.txt"));
    }
}

