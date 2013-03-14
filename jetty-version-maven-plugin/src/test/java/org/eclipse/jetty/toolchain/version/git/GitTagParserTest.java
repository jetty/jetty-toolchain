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

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Assert;
import org.junit.Test;

public class GitTagParserTest extends AbstractGitTestCase
{
    @Test
    public void testGitTagParse() throws IOException, ParseException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-tag.txt");
        GitTagParser parser = new GitTagParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.tagIds",parser.getTagIds());
        Assert.assertEquals("parser.tagIds.size",121,parser.getTagIds().size());
    }
}
