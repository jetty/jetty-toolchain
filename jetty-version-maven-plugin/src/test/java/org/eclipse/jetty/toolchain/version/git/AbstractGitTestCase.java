/*******************************************************************************
 * Copyright (c) 2011 Intalio, Inc.
 * ======================================================================
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *   The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *
 *   The Apache License v2.0 is available at
 *   http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/
package org.eclipse.jetty.toolchain.version.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.plexus.util.IOUtil;

public abstract class AbstractGitTestCase
{
    protected void parseSampleFile(GitOutputParser parser, File sampleFile) throws IOException
    {
        FileReader reader = null;
        BufferedReader buf = null;
        try
        {
            reader = new FileReader(sampleFile);
            buf = new BufferedReader(reader);
            int linenum = 0;
            String line;
            parser.parseStart();
            while ((line = buf.readLine()) != null)
            {
                linenum++;
                parser.parseLine(linenum,line);
            }
            parser.parseEnd();
        }
        finally
        {
            IOUtil.close(buf);
            IOUtil.close(reader);
        }
    }
}
