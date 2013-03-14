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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.eclipse.jetty.toolchain.version.Release;
import org.eclipse.jetty.toolchain.version.VersionPattern;
import org.eclipse.jetty.toolchain.version.VersionText;
import org.eclipse.jetty.toolchain.version.issues.Issue;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class GitLogParserTest extends AbstractGitTestCase
{
    @Rule
    public TestingDir testdir = new TestingDir();

    @Test
    public void testParseGitLogTag() throws IOException, ParseException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-specific-tag.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",1,parser.getGitCommitLogs().size());
        GitCommit commit = parser.getGitCommitLog(0);
        Assert.assertNotNull("parser.getGitCommitLog(0)",commit);
        Assert.assertEquals("commit.id","5aa94f502e5efe68628cff0378f44ff00619c493",commit.getCommitId());
    }

    @Test
    public void testParseIssueIds() throws IOException, ParseException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-to-commit.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        List<Issue> issues = parser.getIssues();
        Assert.assertEquals("Commit entries with Issue IDs",110,issues.size());

        Release rel = new Release("TEST-VERSION");
        rel.setExisting(false);
        rel.addIssues(issues);

        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.addRelease(rel);

        testdir.ensureEmpty();
        File outfile = testdir.getFile("test-ver.txt");
        vt.write(outfile);

        // TODO: compare output
    }

    @Test
    public void testParseLongGitLog() throws IOException, ParseException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-to-commit.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",255,parser.getGitCommitLogs().size());

        int jesseCount = 0;
        for (GitCommit commit : parser.getGitCommitLogs())
        {
            if (commit.getAuthorName().contains("Jesse"))
            {
                jesseCount++;
            }
        }

        Assert.assertEquals("Commits by Jesse",79,jesseCount);
    }
    
    @Test
    public void testParseJetty9GitLog() throws IOException, ParseException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-jetty9-log.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",160,parser.getGitCommitLogs().size());

        int joakimCount = 0;
        for (GitCommit commit : parser.getGitCommitLogs())
        {
            if (commit.getAuthorName().contains("Joakim"))
            {
                joakimCount++;
            }
        }

        Assert.assertEquals("Commits by Joakim",10,joakimCount);
        
        // Test for known issues
        List<String> issueIds = new ArrayList<String>();
        issueIds.add("391483");
        issueIds.add("388079");
        issueIds.add("391588");
        issueIds.add("JETTY-1515");
        
        for(Issue issue: parser.getIssues()) {
            if(issueIds.contains(issue.getId())) {
                issueIds.remove(issue.getId());
            }
            // System.out.printf("Issue[%s] %s%n", issue.getId(), issue.getText());
        }
        
        if(issueIds.size()>0) {
            StringBuilder err = new StringBuilder();
            err.append("Issue parser failed to find issue id");
            if(issueIds.size()>1) {
                err.append("s");
            }
            err.append(":");
            for(String id: issueIds) {
                err.append(" ").append(id);
            }
            err.append(".");
            Assert.assertEquals(err.toString(), 0, issueIds.size());
        }
        
        Assert.assertEquals("Issue count", 42, parser.getIssues().size());
    }

    @Test
    public void testParseSingleGitLog() throws IOException, ParseException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-specific-commit.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",1,parser.getGitCommitLogs().size());
        GitCommit commit = parser.getGitCommitLog(0);
        Assert.assertNotNull("parser.getGitCommitLog(0)",commit);
        Assert.assertEquals("commit.id","596fa1bd4edebc21de0389ff70b10b8060667ed1",commit.getCommitId());
    }
}
