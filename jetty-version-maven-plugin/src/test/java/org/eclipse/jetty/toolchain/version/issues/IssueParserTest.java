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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

@RunWith(Parameterized.class)
public class IssueParserTest
{
    private static class DataList extends ArrayList<String[]>
    {
        public void addCase(String rawline, String expectedID, String expectedText)
        {
            add(new String[]{ rawline, expectedID, expectedText });
        }
    }

    @Parameterized.Parameters
    public static List<String[]> data() {
        DataList data = new DataList();
        data.addCase(" + Fixed client abort",null,"Fixed client abort");
        data.addCase(" - Missing request dispatchers",null,"Missing request dispatchers");
        data.addCase(" * Totally rearchitected and rebuilt",null,"Totally rearchitected and rebuilt");
        data.addCase(" + JETTY-171 Fixed filter mapping","JETTY-171","Fixed filter mapping");
        data.addCase("391623 Add option to --stop to wait for target jetty to stop", "391623", "Add option to --stop to wait for target jetty to stop");
        data.addCase("388079: AbstractHttpConnection. Flush the buffer before shutting output down on error condition", "388079", "AbstractHttpConnection. Flush the buffer before shutting output down on error condition");
        data.addCase("[Bug 388073] null session id from cookie causes NPE fixed", "388073", "null session id from cookie causes NPE fixed");
        data.addCase("Bug 391588 - WebSocket Client does not set masking on close frames", "391588", "WebSocket Client does not set masking on close frames");
        data.addCase("393385: Make hostname verification configurable in SslContextFactory and enable it by default (See http://www.ietf.org/rfc/rfc2818.txt section 3.1)",
                "393385",
                "Make hostname verification configurable in SslContextFactory and enable it by default (See http://www.ietf.org/rfc/rfc2818.txt section 3.1)");
        data.addCase("+ Fixed JETTY-68. Complete request after sendRedirect","JETTY-68","Complete request after sendRedirect");
        // Github specific
        data.addCase("Issue #354 (Spin loop in case of exception thrown during accept()).",
                "354", "Spin loop in case of exception thrown during accept()");
        data.addCase("Issue #84 Ignored test", "84", "Ignored test");
        // Bad git subject line issue
        data.addCase("Issue #123: foo Issue #124: bar",
                "123", "foo Issue #124: bar");

        return data;
    }

    @Parameterized.Parameter(0)
    public String rawline;

    @Parameterized.Parameter(1)
    public String expectedID;

    @Parameterized.Parameter(2)
    public String expectedText;

    @Test
    public void assertKnownIssue()
    {
        IssueParser issueParser = new IssueParser();
        Issue issue = issueParser.parseKnownIssue(rawline);
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
}
