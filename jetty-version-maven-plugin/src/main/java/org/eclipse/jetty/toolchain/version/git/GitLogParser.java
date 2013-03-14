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

import org.eclipse.jetty.toolchain.version.issues.Issue;
import org.eclipse.jetty.toolchain.version.issues.IssueParser;

public class GitLogParser implements GitOutputParser
{
    private static final String AUTHOR_DATE = "#AUTHOR_DATE#:";
    private static final String AUTHOR_NAME = "#AUTHOR_NAME#:";
    private static final String BODY = "#BODY#:";
    private static final String COMMIT_ID = "#COMMIT_ID#:";
    private static final String COMMITTER_DATE = "#COMMITTER_DATE#:";
    private static final String COMMITTER_NAME = "#COMMITTER_NAME#:";
    private static final String END = "####";
    private static final String SUBJECT = "#SUBJECT#:";

    private GitCommit activeCommit;
    private final List<GitCommit> commits = new ArrayList<GitCommit>();

    public String getFormat()
    {
        StringBuilder fmt = new StringBuilder();
        fmt.append("--pretty=format:");
        fmt.append(COMMIT_ID).append("%H%n");
        fmt.append(AUTHOR_NAME).append("%an%n");
        fmt.append(AUTHOR_DATE).append("%ai%n");
        fmt.append(COMMITTER_NAME).append("%cn%n");
        fmt.append(COMMITTER_DATE).append("%ci%n");
        fmt.append(SUBJECT).append("%s%n");
        fmt.append(BODY).append("%b%n");
        fmt.append(END);
        return fmt.toString();
    }

    public GitCommit getGitCommitLog(int index)
    {
        return commits.get(index);
    }

    public List<GitCommit> getGitCommitLogs()
    {
        return commits;
    }

    public List<Issue> getIssues()
    {
        IssueParser issueparser = new IssueParser();
        List<Issue> issues = new ArrayList<Issue>();

        Issue issue;
        for (GitCommit commit : commits)
        {
            issue = issueparser.parsePossibleIssue(commit.getSubject());
            if (issue != null)
            {
                issues.add(issue);
            }
        }

        return issues;
    }

    public void parseEnd()
    {
        if (activeCommit != null)
        {
            commits.add(activeCommit);
        }
    }

    public void parseLine(int linenum, String line) throws ParseException
    {
        if (activeCommit == null)
        {
            activeCommit = new GitCommit();
        }

        if (line.startsWith(COMMIT_ID))
        {
            activeCommit.setCommitId(line.substring(COMMIT_ID.length()));
        }
        else if (line.startsWith(AUTHOR_NAME))
        {
            activeCommit.setAuthorName(line.substring(AUTHOR_NAME.length()));
        }
        else if (line.startsWith(AUTHOR_DATE))
        {
            activeCommit.parseAuthorDate(line.substring(AUTHOR_DATE.length()));
        }
        else if (line.startsWith(COMMITTER_NAME))
        {
            activeCommit.setCommitterName(line.substring(COMMITTER_NAME.length()));
        }
        else if (line.startsWith(COMMITTER_DATE))
        {
            activeCommit.parseCommitterDate(line.substring(COMMITTER_DATE.length()));
        }
        else if (line.startsWith(SUBJECT))
        {
            activeCommit.setSubject(line.substring(SUBJECT.length()));
        }
        else if (line.startsWith(BODY))
        {
            activeCommit.setBody(line.substring(BODY.length()));
        }
        else if (line.equals(END))
        {
            commits.add(activeCommit);
            activeCommit = null;
        }
        else
        {
            activeCommit.appendBody(line.trim());
        }
    }

    public void parseStart()
    {
        commits.clear();
    }
}
