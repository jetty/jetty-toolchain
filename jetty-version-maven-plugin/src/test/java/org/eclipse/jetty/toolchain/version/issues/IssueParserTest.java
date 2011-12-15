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
package org.eclipse.jetty.toolchain.version.issues;

import org.junit.Assert;
import org.junit.Test;

public class IssueParserTest
{
    private final IssueParser issueParser = new IssueParser();

    private void assertKnownIssue(String rawissue, String expectedID, String expectedText)
    {
        Issue issue = issueParser.parseKnownIssue(rawissue);
        Assert.assertNotNull("Should always result in a issue",issue);
        if (expectedID == null)
        {
            Assert.assertFalse("Should not have an issue.id",issue.hasId());
        }
        else
        {
            Assert.assertTrue("Should have an issue.id",issue.hasId());
            Assert.assertEquals("issue.id",expectedID,issue.getId());
        }
        Assert.assertEquals("issue.text",expectedText,issue.getText());
    }

    @Test
    public void testParseKnownBullet()
    {
        assertKnownIssue(" + Fixed client abort",null,"Fixed client abort");
    }

    @Test
    public void testParseKnownBulletDash()
    {
        assertKnownIssue(" - Missing request dispatchers",null,"Missing request dispatchers");
    }

    @Test
    public void testParseKnownBulletSplat()
    {
        assertKnownIssue(" * Totally rearchitected and rebuilt",null,"Totally rearchitected and rebuilt");
    }

    @Test
    public void testParseKnownEarlyJetty()
    {
        assertKnownIssue(" + JETTY-171 Fixed filter mapping","JETTY-171","Fixed filter mapping");
    }

    @Test
    public void testParseKnownEarlyJettyOdd()
    {
        assertKnownIssue("+ Fixed JETTY-68. Complete request after sendRedirect","JETTY-68","Complete request after sendRedirect");
    }
}