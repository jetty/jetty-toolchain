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

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import static org.eclipse.jetty.toolchain.test.PathMatchers.isDirectory;
import static org.eclipse.jetty.toolchain.test.PathMatchers.isRegularFile;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * IO Utilities.
 */
public final class IO
{
    @SuppressWarnings("javadoc")
    public static final int BUFFER_SIZE = 64 * 1024;

    private IO()
    {
        /* prevent instantiation */
    }

    /**
     * Copy Reader to Writer out until EOF or exception.
     *
     * @param in the Reader to read from
     * @param out the Writer to write to
     * @throws IOException if unable to copy the contents
     */
    public static void copy(Reader in, Writer out) throws IOException
    {
        char buffer[] = new char[BUFFER_SIZE];
        int len = BUFFER_SIZE;

        while (true)
        {
            len = in.read(buffer, 0, BUFFER_SIZE);
            if (len == -1)
            {
                break;
            }
            out.write(buffer, 0, len);
        }
    }

    /**
     * Copy the entire {@link InputStream} to the {@link OutputStream}
     *
     * @param in the input stream to read from
     * @param out the output stream to write to
     * @throws IOException if unable to copy the stream
     */
    public static void copy(InputStream in, OutputStream out) throws IOException
    {
        byte buffer[] = new byte[BUFFER_SIZE];
        int len = BUFFER_SIZE;

        while (true)
        {
            len = in.read(buffer, 0, BUFFER_SIZE);
            if (len < 0)
            {
                break;
            }
            out.write(buffer, 0, len);
        }
    }

    /**
     * closes an {@link Closeable}, and silently ignores exceptions
     *
     * @param c the closeable to close
     */
    public static void close(Closeable c)
    {
        if (c == null)
        {
            return;
        }

        try
        {
            c.close();
        }
        catch (IOException ignore)
        {
            /* ignore */
        }
    }

    /**
     * Copy files or directories.
     *
     * @param src the from path
     * @param dest the destination path
     * @throws IOException if unable to copy the file
     */
    public static void copy(Path src, Path dest) throws IOException
    {
        if (Files.isDirectory(src))
        {
            copyDir(src, dest);
        }
        else
        {
            copyFile(src, dest);
        }
    }

    /**
     * Copy the contents of a directory from one directory to another.
     *
     * @param srcDir the from directory
     * @param destDir the destination directory
     * @throws IOException if unable to copy the file
     */
    public static void copyDir(Path srcDir, Path destDir) throws IOException
    {
        Objects.requireNonNull(srcDir);
        Objects.requireNonNull(destDir);
        assertThat("Source Dir", srcDir, isDirectory());
        assertThat("Destination Dir", destDir, isDirectory());
        assertThat("Destination Dir", destDir, PathMatchers.isEmptyDirectory());

        System.out.printf("CopyDir %s to %s%n", srcDir, destDir);

        try (Stream<Path> sourceStream = Files.walk(srcDir, 20))
        {
            Iterator<Path> iterFiles = sourceStream
                .filter(Files::isRegularFile)
                .iterator();
            while (iterFiles.hasNext())
            {
                Path sourceFile = iterFiles.next();
                Path destFile = destDir.resolve(srcDir.relativize(sourceFile));
                if (!Files.exists(destFile.getParent()))
                    Files.createDirectories(destFile.getParent());
                Files.copy(sourceFile, destFile);
            }
        }
    }

    /**
     * Copy a file from one place to another using {@link Files#copy(Path, Path, CopyOption...)}
     *
     * @param srcFile the file to copy
     * @param destFile the destination file to create
     * @throws IOException if unable to copy the file
     */
    public static void copyFile(Path srcFile, Path destFile) throws IOException
    {
        assertThat("Source File", srcFile, isRegularFile());
        Files.copy(srcFile, destFile);
    }

    // --- EVERYTHING BELOW THIS LINE IS DEPRECATED ---

    /**
     * @deprecated use {@link Files#readString(Path, Charset)} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static String readToString(Path path) throws IOException
    {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * Read the contents of a file into a String and return it.
     *
     * @param file the file to read.
     * @return the contents of the file.
     * @throws IOException if unable to read the file.
     * @deprecated use {@link Files#readString(Path, Charset)} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static String readToString(File file) throws IOException
    {
        return Files.readString(file.toPath(), StandardCharsets.UTF_8);
    }

    /**
     * Copy files or directories.
     *
     * @param from the from path
     * @param to the destination path
     * @throws IOException if unable to copy the file
     * @deprecated use {@link #copy(Path, Path)} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void copy(File from, File to) throws IOException
    {
        if (from.isDirectory())
        {
            copyDir(from, to);
        }
        else
        {
            copyFile(from, to);
        }
    }

    /**
     * Copy the contents of a directory from one directory to another.
     *
     * @param from the from directory
     * @param to the destination directory
     * @throws IOException if unable to copy the file
     * @deprecated use {@link #copyDir(Path, Path)} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void copyDir(File from, File to) throws IOException
    {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        assertThat(to.toPath(), isDirectory());

        for (File file : from.listFiles(IO.SafeFileFilter.INSTANCE))
        {
            copy(file, new File(to, file.getName()));
        }
    }

    /**
     * A {@link FileFilter} for obtaining a list of contents that does not contain the special
     * <code>.</code> and <code>..</code> entries that some JVM environments report.
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static class SafeFileFilter implements FileFilter
    {
        @SuppressWarnings("javadoc")
        public static final SafeFileFilter INSTANCE = new SafeFileFilter();

        public boolean accept(File path)
        {
            String name = path.getName();
            if (".".equals(name) || "..".equals(name))
            {
                return false; // old school.
            }
            return (path.isFile() || path.isDirectory());
        }
    }

    /**
     * Copy a file from one place to another
     *
     * @param from the file to copy
     * @param to the destination file to create
     * @throws IOException if unable to copy the file
     * @deprecated use {@link Files#copy(Path, Path, CopyOption...)} instead
     */
    @Deprecated
    public static void copyFile(File from, File to) throws IOException
    {
        Files.copy(from.toPath(), to.toPath());
    }
}
