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
package org.eclipse.jetty.toolchain.version;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.jetty.toolchain.version.issues.Issue;
import org.eclipse.jetty.toolchain.version.issues.IssueComparator;

import edu.emory.mathcs.backport.java.util.Collections;

public class Release
{
    private static final List<String> RELEASED_ON_FORMATS;

    static
    {
        RELEASED_ON_FORMATS = new ArrayList<String>();
        RELEASED_ON_FORMATS.add("M/d/yyyy"); // USA Format (shorthand)
        RELEASED_ON_FORMATS.add("EEE d MMMM yyyy"); // USA Format w/Weekday
        RELEASED_ON_FORMATS.add("d MMMM yyyy"); // USA Format
        RELEASED_ON_FORMATS.add("MMMM d yyyy"); // Month Day Year
        RELEASED_ON_FORMATS.add("MMMM yyyy"); // Month Year
    }

    private boolean existing = false;
    private List<Issue> issues = new ArrayList<Issue>();
    private Date releasedOn;
    private String version;

    public Release()
    {
        /* anonymous version */
    }

    public Release(String ver)
    {
        this.version = ver;
    }

    public void addIssue(Issue issue)
    {
        if (issue == null)
        {
            return;
        }

        if (!issues.contains(issue))
        {
            this.issues.add(issue);
        }
    }

    public void addIssues(List<Issue> moreIssues)
    {
        for (Issue issue : moreIssues)
        {
            addIssue(issue);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Release other = (Release)obj;
        if (version == null)
        {
            if (other.version != null)
            {
                return false;
            }
        }
        else if (!version.equals(other.version))
        {
            return false;
        }
        return true;
    }

    public List<Issue> getIssues()
    {
        return issues;
    }

    public Date getReleasedOn()
    {
        return releasedOn;
    }

    public List<Issue> getSortedIssues()
    {
        Collections.sort(issues,new IssueComparator());
        return issues;
    }

    public String getVersion()
    {
        return version;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((version == null)?0:version.hashCode());
        return result;
    }

    public boolean isExisting()
    {
        return existing;
    }

    public void parseReleasedOn(int linenum, String rawdateStr)
    {
        if (StringUtils.isBlank(rawdateStr))
        {
            releasedOn = null;
            return;
        }

        String rawdate = rawdateStr.trim();
        if (rawdate.startsWith("- "))
        {
            rawdate = rawdate.substring(2);
        }

        // Strip Ordinals
        String ordinals[] = new String[]
        { "st", "nd", "rd", "th" };
        String simp;
        Pattern ordPat;
        Matcher mat;
        for (String ordinal : ordinals)
        {
            ordPat = Pattern.compile("[0-9]" + ordinal);
            mat = ordPat.matcher(rawdate);
            if (mat.find())
            {
                simp = rawdate.substring(0,mat.start()) + rawdate.charAt(mat.start()) + rawdate.substring(mat.end());
                rawdate = simp;
            }
        }

        // Attempt to parse date
        SimpleDateFormat sdf;
        for (String format : RELEASED_ON_FORMATS)
        {
            sdf = new SimpleDateFormat(format);
            try
            {
                releasedOn = sdf.parse(rawdate);
                if (releasedOn != null)
                {
                    return; // Got a valid date.
                }
            }
            catch (ParseException ignore)
            {
                /* ignore */
            }
        }

        if (releasedOn == null)
        {
            System.err.printf("ERROR: Unable to parse raw date string [%s] on line #%d%n",rawdate,linenum);
        }
    }

    public void setExisting(boolean existing)
    {
        this.existing = existing;
    }

    public void setIssues(List<Issue> issues)
    {
        this.issues.clear();
        this.issues.addAll(issues);
    }

    public void setReleasedOn(Date releasedOn)
    {
        this.releasedOn = releasedOn;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    @Override
    public String toString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("Release[");
        buf.append("version=").append(version);
        buf.append(",releasedOn=");
        if (releasedOn == null)
        {
            buf.append("<null>");
        }
        else
        {
            buf.append(new SimpleDateFormat("MMM yyyy").format(releasedOn));
        }
        buf.append(",issues.size=").append(issues.size());
        buf.append("]");
        return buf.toString();
    }
}
