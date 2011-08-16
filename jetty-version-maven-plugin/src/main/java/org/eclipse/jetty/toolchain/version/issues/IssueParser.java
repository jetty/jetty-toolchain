package org.eclipse.jetty.toolchain.version.issues;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.plexus.util.StringUtils;

public class IssueParser
{
    private final List<Pattern> issue_id_patterns;

    public IssueParser()
    {
        issue_id_patterns = new ArrayList<Pattern>();
        issue_id_patterns.add(Pattern.compile("^.[Bb]ug ([0-9]{6,}). ")); // Bugzilla id (from mylyn)
        issue_id_patterns.add(Pattern.compile("^([0-9]{6,}) ")); // Bugzilla id
        issue_id_patterns.add(Pattern.compile("^.[Bb]ug (JETTY-[0-9]{2,}). ")); // Jira id (from mylyn)
        issue_id_patterns.add(Pattern.compile("(JETTY-[0-9]{2,})[^0-9]")); // Jira id
    }

    /**
     * Parse a known issue (such as " + 341235 Bug Text Goes Here")
     */
    public Issue parseKnownIssue(String rawissue)
    {
        String raw = rawissue;

        // Eliminate known bullet types
        raw = raw.replaceFirst("^ *[\\*\\+-] *","");
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
                // Cleanup Subject Line
                subject = subject.substring(mat.end());
                if (subject.startsWith("- "))
                {
                    subject = subject.substring(2);
                }
                return new Issue(id,subject.trim());
            }
        }

        return null;
    }
}
