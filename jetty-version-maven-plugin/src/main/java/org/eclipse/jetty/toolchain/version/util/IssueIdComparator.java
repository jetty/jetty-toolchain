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
package org.eclipse.jetty.toolchain.version.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

public class IssueIdComparator implements Comparator<String>
{
    // Use natural language ordering.
    private final Collator collator = Collator.getInstance();

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
