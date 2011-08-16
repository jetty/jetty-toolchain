package org.eclipse.jetty.toolchain.version.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

public class IssueIdComparator implements Comparator<String>
{
    // Use natural language ordering.
    private final Collator collator = Collator.getInstance();

    @Override
    public int compare(String o1, String o2)
    {
        CollationKey key1 = toKey(o1);
        CollationKey key2 = toKey(o2);
        return key1.compareTo(key2);
    }

    private CollationKey toKey(String str)
    {
        if (str == null)
        {
            return collator.getCollationKey("");
        }
        return collator.getCollationKey(str.trim());
    }
}
