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
import java.util.Date;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.jetty.toolchain.version.util.DateUtil;

public class GitCommit
{
    private Date authorDate;
    private String authorName;
    private String body;
    private String commitId;
    private Date committerDate;
    private String committerName;
    private String subject;

    public void appendBody(String trim)
    {
        // TODO Auto-generated method stub

    }

    public Date getAuthorDate()
    {
        return authorDate;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public String getBody()
    {
        return body;
    }

    public String getCommitId()
    {
        return commitId;
    }

    public Date getCommitterDate()
    {
        return committerDate;
    }

    public String getCommitterName()
    {
        return committerName;
    }

    public String getSubject()
    {
        return subject;
    }

    public void parseAuthorDate(String rawdate) throws ParseException
    {
        if (StringUtils.isBlank(rawdate))
        {
            authorDate = null;
            return;
        }

        authorDate = DateUtil.parseIso8601(rawdate);
    }

    public void parseCommitterDate(String rawdate) throws ParseException
    {
        if (StringUtils.isBlank(rawdate))
        {
            committerDate = null;
            return;
        }

        committerDate = DateUtil.parseIso8601(rawdate);
    }

    public void setAuthorDate(Date authorDate)
    {
        this.authorDate = authorDate;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public void setCommitId(String commitId)
    {
        this.commitId = commitId;
    }

    public void setCommitterDate(Date committerDate)
    {
        this.committerDate = committerDate;
    }

    public void setCommitterName(String committerName)
    {
        this.committerName = committerName;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }
}
