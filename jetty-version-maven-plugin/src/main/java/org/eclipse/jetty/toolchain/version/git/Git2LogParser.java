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

import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

public class Git2LogParser implements GitOutputParser
{
    private Log log;
    private String commandId;

    public Git2LogParser(Log log, String commandId)
    {
        this.log = log;
        this.commandId = commandId;
    }

    @Override
    public void parseEnd()
    {
        /* ignore */
    }

    @Override
    public void parseLine(int linenum, String line) throws IOException
    {
        log.debug("[" + commandId + "]: " + line);
    }

    @Override
    public void parseStart()
    {
        /* ignore */
    }
}
