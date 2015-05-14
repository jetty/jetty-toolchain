//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.toolchain.shade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import org.apache.maven.plugins.shade.relocation.Relocator;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;
import org.codehaus.plexus.util.IOUtil;

/**
 * Include arbitrary directory in shaded jar
 */
public class IncludeDirectoryTransformer implements ResourceTransformer
{
    public static boolean DEBUG = false;

    private static class DirectoryCopier extends SimpleFileVisitor<Path>
    {
        private final Path basePath;
        private final JarOutputStream jos;

        public DirectoryCopier(Path basePath, JarOutputStream jos)
        {
            this.basePath = basePath;
            this.jos = jos;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
        {
            String entryName = basePath.relativize(file).toString();
            debug("entryName = %s",entryName);
            
            jos.putNextEntry(new JarEntry(entryName));
            
            InputStream in = Files.newInputStream(file, StandardOpenOption.READ);
            IOUtil.copy(in,jos);
            IOUtil.close(in);
            
            return super.visitFile(file,attrs);
        }

        public void debug(String format, Object... args)
        {
            if (DEBUG)
            {
                System.err.printf("## " + this.getClass().getSimpleName() + " " + format + "%n",args);
            }
        }
    }

    File directory;

    public boolean canTransformResource(String resource)
    {
        return false;
    }

    public boolean hasTransformedResource()
    {
        return directory != null ? directory.exists() : false;
    }

    public void modifyOutputStream(JarOutputStream jos) throws IOException
    {
        // Copy all of the resources in directory to JAR
        Path basePath = directory.toPath();
        Files.walkFileTree(basePath,new DirectoryCopier(basePath,jos));
    }

    public void processResource(String resource, InputStream is, List<Relocator> relocators) throws IOException
    {
        // do nothing
    }
}
