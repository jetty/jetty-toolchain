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


package org.eclipse.jetty.toolchain.shade;


import org.codehaus.plexus.util.IOUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;

public class MergeFilesTransformerTest
{
    @Test
    public void mergeToSeparateFile()
        throws Exception
    {
        List<String> files =
            Arrays.asList( "target/test-classes/META-INF/LICENSE", "target/test-classes/META-INF/LICENSE.txt" );
        MergeFilesTransformer mergeFilesTransformer = new MergeFilesTransformer( files, "LICENSE", false );
        Path result = Files.createTempFile( "result_transform", ".jetty_tmp.zip" );

        try (JarOutputStream outputStream = new JarOutputStream( Files.newOutputStream( result ) ))
        {
            mergeFilesTransformer.modifyOutputStream( outputStream );
        }

        try (JarInputStream jarInputStream = new JarInputStream( Files.newInputStream( result ) ))
        {
            jarInputStream.getNextJarEntry();
            StringWriter stringWriter = new StringWriter();
            IOUtil.copy( jarInputStream, stringWriter );
            String content = stringWriter.toString();
            // must contain
            // COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0
            Assertions.assertTrue(
                content.contains( "COMMON DEVELOPMENT AND DISTRIBUTION LICENSE (CDDL) Version 1.0" ) );
            // Apache License
            Assertions.assertTrue( content.contains( "Apache License" ) );
            Assertions.assertTrue( content.contains( "Version 2.0, January 2004"));
            Assertions.assertTrue( content.contains( "\"CLASSPATH\" EXCEPTION TO THE GPL VERSION 2"));
            // Eclipse Public License - v 1.0
            Assertions.assertTrue( content.contains( "Eclipse Public License - v 1.0" ) );
        } finally
        {
            Files.deleteIfExists( result );
        }
    }
}
