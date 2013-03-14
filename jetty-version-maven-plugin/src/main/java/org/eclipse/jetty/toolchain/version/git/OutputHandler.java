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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.IOUtil;

public class OutputHandler extends Thread
{
    private final InputStream in;
    private final Log log;
    private final GitOutputParser parser;

    public OutputHandler(Log log, InputStream in, GitOutputParser parser)
    {
        this.log = log;
        this.in = in;
        this.parser = parser;
    }

    @Override
    public void run()
    {
        int linenum = 0;
        parser.parseStart();
        InputStreamReader reader = null;
        BufferedReader buf = null;
        try
        {
            reader = new InputStreamReader(in);
            buf = new BufferedReader(reader);
            String line;
            while ((line = buf.readLine()) != null)
            {
                linenum++;
                parser.parseLine(linenum,line);
            }
        }
        catch (ParseException e)
        {
            log.debug(e);
        }
        catch (IOException e)
        {
            if (!e.getMessage().equalsIgnoreCase("Stream closed"))
            {
                log.debug(e);
            }
        }
        finally
        {
            IOUtil.close(buf);
            IOUtil.close(reader);
        }
        parser.parseEnd();
        log.debug("Parsed " + linenum + " lines of output");
    }
}