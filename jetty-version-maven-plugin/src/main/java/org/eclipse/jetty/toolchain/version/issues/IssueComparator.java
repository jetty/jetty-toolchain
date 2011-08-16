package org.eclipse.jetty.toolchain.version.issues;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

public class IssueComparator implements Comparator<Issue>
{
    private final Collator collator = Collator.getInstance();

    @Override
    public int compare(Issue o1, Issue o2)
    {
        CollationKey key1 = toKey(o1);
        CollationKey key2 = toKey(o2);
        return key1.compareTo(key2);
    }

    private CollationKey toKey(Issue issue)
    {
        if ((issue == null) || (issue.getId() == null))
        {
            return collator.getCollationKey("");
        }
        if (issue.getId().startsWith("JETTY-"))
        {
            try
            {
                Integer num = Integer.parseInt(issue.getId().substring(6));
                return collator.getCollationKey(String.format("JETTY-%06d",num));
            }
            catch (NumberFormatException e)
            {
                return collator.getCollationKey(issue.getId());
            }
        }
        else
        {
            return collator.getCollationKey(issue.getId());
        }
    }
}
