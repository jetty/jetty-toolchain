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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * Basic functions for working with JAR files in test cases.
 */
public final class JAR
{
    private JAR()
    {
        /* prevent instantiation */
    }

    /**
     * Unpack JAR file into destination directory.
     *
     * @param jarFile the jar file to unpack
     * @param destDir the destination directory to unpack into
     * @throws IOException if unable to unpack jar file.
     */
    public static void unpack(File jarFile, File destDir) throws IOException
    {
        PathAssert.assertFileExists("Jar File", jarFile);
        PathAssert.assertDirExists("Destination Path", destDir);

        JarFile jar = null;
        try
        {
            jar = new JarFile(jarFile);

            File destFile;
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements())
            {
                JarEntry entry = entries.nextElement();
                destFile = new File(destDir, FS.separators(entry.getName()));
                if (entry.isDirectory())
                {
                    FS.ensureDirExists(destFile);
                }
                else
                {
                    unpack(jar, entry, destFile);
                }
            }
        }
        finally
        {
            close(jar);
        }
    }

    private static void unpack(JarFile jar, JarEntry entry, File destFile) throws IOException
    {
        InputStream in = null;
        FileOutputStream out = null;
        try
        {
            in = jar.getInputStream(entry);
            if (!destFile.getParentFile().exists())
            {
                destFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(destFile, false);
            IO.copy(in, out);
        }
        finally
        {
            IO.close(in);
            IO.close(out);
        }
    }

    /**
     * Close a JAR file.
     *
     * @param jar the JarFile to close
     */
    public static void close(JarFile jar)
    {
        if (jar == null)
        {
            return;
        }

        try
        {
            jar.close();
        }
        catch (IOException ignore)
        {
            /* ignore */
        }
    }

    /**
     * Create a JAR file out of the contents of a specific directory (recursively)
     *
     * @param srcDir the source directory
     * @param jarFile the destination jar file to create
     * @throws IOException if unable to create the jar file, or read the source directory
     */
    public static void create(File srcDir, File jarFile) throws IOException
    {
        JarOutputStream jarout = null;
        FileOutputStream fileout = null;
        try
        {
            fileout = new FileOutputStream(jarFile);
            jarout = new JarOutputStream(fileout);

            packDir(srcDir, srcDir, jarout);
        }
        finally
        {
            IO.close(jarout);
            IO.close(fileout);
        }
    }

    private static void packDir(File baseDir, File srcDir, JarOutputStream jarout) throws IOException
    {
        URI baseURI = baseDir.toURI();

        for (File file : srcDir.listFiles(IO.SafeFileFilter.INSTANCE))
        {
            if (file.isDirectory())
            {
                packDir(baseDir, file, jarout);
            }
            else
            {
                String name = baseURI.relativize(file.toURI()).toASCIIString();
                JarEntry entry = new JarEntry(name);
                entry.setSize(file.length());
                InputStream in = null;
                try
                {
                    in = new FileInputStream(file);
                    jarout.putNextEntry(entry);
                    IO.copy(in, jarout);
                }
                finally
                {
                    jarout.closeEntry();
                    IO.close(in);
                }
            }
        }
    }
}
