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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringUtils;
import org.eclipse.jetty.toolchain.version.issues.Issue;
import org.eclipse.jetty.toolchain.version.issues.IssueParser;

public class VersionText
{
    private boolean sortExisting = false;
    private VersionPattern versionPattern;
    private List<String> headers = new ArrayList<String>();
    private final LinkedList<Release> releases = new LinkedList<Release>();

    public VersionText(VersionPattern pat)
    {
        this.versionPattern = pat;
    }

    public void addRelease(Release rel)
    {
        releases.add(rel);
    }

    public Release findRelease(String version)
    {
        if (StringUtils.isBlank(version))
        {
            return null;
        }

        for (Release release : releases)
        {
            if (release.getVersion().equals(version))
            {
                return release;
            }
        }

        return null;
    }

    public String getPriorVersion(String currentVersion)
    {
        if (releases.isEmpty())
        {
            return null;
        }

        Iterator<Release> reliter = releases.iterator();
        while (reliter.hasNext())
        {
            Release rel = reliter.next();
            if (rel.getVersion().equals(currentVersion))
            {
                rel = reliter.next();
                if (rel != null)
                {
                    return rel.getVersion();
                }
            }
        }

        return null;
    }

    public List<Release> getReleases()
    {
        return releases;
    }

    public List<String> getVersionList()
    {
        List<String> versions = new ArrayList<String>();
        for (Release rel : releases)
        {
            versions.add(rel.getVersion());
        }
        return versions;
    }

    public VersionPattern getVersionPattern()
    {
        return versionPattern;
    }

    public boolean isSortExisting()
    {
        return sortExisting;
    }

    public void prepend(Release rel)
    {
        releases.add(0,rel);
    }

    public void read(File versionTextFile) throws IOException
    {
        FileReader reader = null;
        BufferedReader buf = null;
        try
        {
            reader = new FileReader(versionTextFile);
            buf = new BufferedReader(reader);

            Pattern patBullet = Pattern.compile(IssueParser.REGEX_ISSUE_BULLET);
            Matcher mat;

            releases.clear();
            Release release = null;
            Issue issue = null;
            IssueParser issueParser = new IssueParser();

            int linenum = 0;
            String line;
            while ((line = buf.readLine()) != null)
            {
                linenum++;
                if (StringUtils.isBlank(line))
                {
                    continue; // skip
                }

                if (line.charAt(0) == '#')
                {
                    // It's a comment
                    headers.add(line);
                    continue;
                }

                if (versionPattern.isMatch(line))
                {
                    // Found a jetty version header!
                    if (release != null)
                    {
                        release.addIssue(issue);
                    }
                    issue = null;

                    if (release != null)
                    {
                        releases.add(release);
                    }

                    // Build a clean and consistent version string
                    String cleanVersion = versionPattern.getLastVersion();
                    release = new Release(cleanVersion.toString());
                    release.setExisting(true);

                    String on = versionPattern.getRemainingText();
                    release.parseReleasedOn(linenum,on);
                    continue;
                }

                mat = patBullet.matcher(line);
                if (mat.find())
                {
                    // Start of an issue text.
                    release.addIssue(issue);
                    issue = issueParser.parseKnownIssue(line);
                }
                else
                {
                    if (issue == null)
                    {
                        issue = issueParser.parseKnownIssue(line);
                    }
                    else
                    {
                        issue.appendText(line);
                    }
                }
            }
            if (release != null)
            {
                if (issue != null)
                {
                    release.addIssue(issue);
                }
                releases.add(release);
            }
        }
        finally
        {
            IOUtil.close(buf);
            IOUtil.close(reader);
        }
    }

    public void replaceOrPrepend(Release rel)
    {
        int indexExisting = releases.indexOf(rel);
        if (indexExisting == (-1))
        {
            releases.add(0,rel); // prepend
        }
        else
        {
            releases.set(indexExisting,rel); // replace
        }
    }

    public void setReleases(List<Release> releases)
    {
        this.releases.addAll(releases);
    }

    public void setSortExisting(boolean allowExistingResort)
    {
        this.sortExisting = allowExistingResort;
    }

    public void write(File versionTextFile) throws IOException
    {
        FileWriter writer = null;
        PrintWriter out = null;
        try
        {
            writer = new FileWriter(versionTextFile);
            out = new PrintWriter(writer);

            if (!headers.isEmpty())
            {
                for (String line : headers)
                {
                    out.println(line);
                }
                out.println();
            }

            for (Release release : releases)
            {
                out.print(release.getVersion());
                if (release.getReleasedOn() != null)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat(" - dd MMMM yyyy");
                    out.print(sdf.format(release.getReleasedOn()));
                }
                out.println();

                if (sortExisting)
                {
                    // All releases are sorted.
                    for (Issue issue : release.getSortedIssues())
                    {
                        out.println(issue.toString());
                    }
                }
                else
                {
                    if (release.isExisting())
                    {
                        // Don't sort existing.
                        for (Issue issue : release.getIssues())
                        {
                            out.println(issue.toString());
                        }
                    }
                    else
                    {
                        // Sort new
                        for (Issue issue : release.getSortedIssues())
                        {
                            out.println(issue.toString());
                        }
                    }
                }
                out.println();
            }
        }
        finally
        {
            IOUtil.close(out);
            IOUtil.close(writer);
        }
    }
}
