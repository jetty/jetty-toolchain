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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;

public class GitTagParser implements GitOutputParser
{
    private final List<String> tagIds = new ArrayList<String>();

    public List<String> getTagIds()
    {
        return tagIds;
    }

    public void parseEnd()
    {
        /* ignore */
    }

    public void parseLine(int linenum, String line) throws ParseException
    {
        if (StringUtils.isNotBlank(line))
        {
            tagIds.add(line.trim());
        }
    }

    public void parseStart()
    {
        /* ignore */
    }
}
