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

import org.apache.maven.plugins.shade.relocation.Relocator;
import org.apache.maven.plugins.shade.resource.ResourceTransformer;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * <p>
 * This transformer (used by Maven Shade plugin will merge few files {@link #files} content
 * in the {@link #resource} within the shaded jar.
 * </p>
 * <p>
 * Per default the current content of the resource (if existing) will preserved.
 * This can be changed using the parameter {@link #preserveResourceContent}
 * </p>
 */
public class MergeFilesTransformer
    implements ResourceTransformer
{

    /**
     * paths to files to merge in the jar
     */
    private List<String> files;

    /**
     * the resource in the jar file to generate/modify
     */
    private String resource;

    /**
     * if the content of the resource must be preserve
     * <code>true</code> per default
     */
    private boolean preserveResourceContent = true;

    private ByteArrayOutputStream data = new ByteArrayOutputStream();

    public MergeFilesTransformer()
    {
        //no op
    }

    public MergeFilesTransformer( List<String> files, String resource, boolean preserveResourceContent )
    {
        this.files = files;
        this.resource = resource;
        this.preserveResourceContent = preserveResourceContent;
    }

    @Override
    public boolean canTransformResource( String resource )
    {
        return StringUtils.equals( this.resource, resource );
    }

    @Override
    public void processResource( String resource, InputStream is, List<Relocator> relocators )
        throws IOException
    {
        if ( preserveResourceContent )
        {
            IOUtil.copy( is, data );
            data.write( '\n' );
        }
    }

    @Override
    public boolean hasTransformedResource()
    {
        // we need to force shade plugin to modify the JarOutputStream
        return files.size() > 0;
    }

    @Override
    public void modifyOutputStream( JarOutputStream jos )
        throws IOException
    {
        for ( String file : files )
        {
            Files.copy( Paths.get( file ), data );
            data.write( '\n' );
        }

        jos.putNextEntry( new JarEntry( resource ) );

        IOUtil.copy( new ByteArrayInputStream( data.toByteArray() ), jos );
        data.reset();
    }
}
