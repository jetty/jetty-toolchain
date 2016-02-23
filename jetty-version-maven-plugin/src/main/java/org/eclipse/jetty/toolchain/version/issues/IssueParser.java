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

import org.codehaus.plexus.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IssueParser
{
    public static final String REGEX_ISSUE_BULLET = "^ [\\*\\+-] ";
    private final List<Pattern> issue_id_patterns;

    public IssueParser()
    {
        // Possible delimitors between issue id and text
        String DELIM = "[-\\[\\]: ]*";

        issue_id_patterns = new ArrayList<Pattern>();
        // Github Based
        issue_id_patterns.add(Pattern.compile("^[\\[\\s]*[I]ssue #([0-9]{2,})" + DELIM));
        // Bugzilla Based
        issue_id_patterns.add(Pattern.compile("^[\\[\\s]*[Bb]ug ([0-9]{6,})" + DELIM));
        issue_id_patterns.add(Pattern.compile("^([0-9]{6,})" + DELIM));
        // Jira Based
        issue_id_patterns.add(Pattern.compile("^[\\[\\s]*[Bb]ug (JETTY-[0-9]{2,})" + DELIM));
        issue_id_patterns.add(Pattern.compile("(JETTY-[0-9]{2,})[^0-9]"));
    }

    /**
     * Parse a known issue (such as " + 341235 Bug Text Goes Here")
     */
    public Issue parseKnownIssue(String rawissue)
    {
        String raw = rawissue;

        // Eliminate known bullet types
        raw = raw.replaceFirst(REGEX_ISSUE_BULLET,"");
        if (StringUtils.isBlank(raw))
        {
            return null;
        }

        Issue issue = parsePossibleIssue(raw);
        if (issue != null)
        {
            return issue;
        }

        raw = raw.trim();
        // Special "zz-" format is to indicate that this Issue object has no issue.id
        // But we declare one anyway so for equals/hashcode/sorting reasons.
        String specialId = "zz-" + raw.substring(0,Math.min(raw.length(),70)).toLowerCase();
        return new Issue(specialId,raw);
    }

    /**
     * Parse a possible issue, if provided line has no Issue ID pattern match, a null is returned.
     */
    public Issue parsePossibleIssue(String rawissue)
    {
        if (StringUtils.isBlank(rawissue))
        {
            return null;
        }

        Matcher mat;
        String subject, id;
        for (Pattern pat : issue_id_patterns)
        {
            subject = rawissue.trim();
            mat = pat.matcher(subject);
            if (mat.find())
            {
                id = mat.group(1);
                subject = subject.substring(mat.end());
                // Cleanup Subject Line
                subject = cleanSubjectLine(subject);
                return new Issue(id,subject);
            }
        }

        return null;
    }

    private String cleanSubjectLine(String subject)
    {
        if (subject.startsWith("- "))
        {
            subject = subject.substring(2);
        }

        Pattern endPunctuation = Pattern.compile("^(.*)\\s*[\\.!,]+\\s*$");
        Matcher mat = endPunctuation.matcher(subject);
        if (mat.matches())
        {
            subject = mat.group(1);
        }

        Pattern parenWrapped = Pattern.compile("^\\s*\\((.*)\\)\\s*$");
        mat = parenWrapped.matcher(subject);
        if (mat.matches())
        {
            subject = mat.group(1);
        }

        return subject.trim();
    }
}
