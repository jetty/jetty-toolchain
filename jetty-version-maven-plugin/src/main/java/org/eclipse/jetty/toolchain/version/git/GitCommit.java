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
