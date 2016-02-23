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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionPattern
{
    public static final VersionPattern ECLIPSE = new VersionPattern("jetty-VERSION");
    public static final VersionPattern CODEHAUS = new VersionPattern("jetty@codehaus-VERSION");

    private final String key;
    private final Pattern pat;
    private String lastVersion;
    private String remainingText;

    public VersionPattern(String key)
    {
        this.key = key;
        String regex = key.replace("-","[- ]").replace("VERSION","([1-9]\\.[0-9]{1,}[^ ]*)");
        this.pat = Pattern.compile("^" + regex,Pattern.CASE_INSENSITIVE);
    }

    public String getLastVersion()
    {
        if (lastVersion == null)
        {
            return null;
        }
        return key.replace("VERSION",lastVersion);
    }

    public String getLastVersion(String altkey)
    {
        if (lastVersion == null)
        {
            return null;
        }
        return altkey.replace("VERSION",lastVersion);
    }

    public String getRemainingText()
    {
        return remainingText;
    }

    public boolean isMatch(String str)
    {
        Matcher mat = pat.matcher(str);
        if (mat.find())
        {
            remainingText = str.substring(mat.end());
            lastVersion = mat.group(1);
            return true;
        }
        else
        {
            remainingText = null;
            lastVersion = null;
        }
        return false;
    }

    public String toVersionId(String rawversion)
    {
        return key.replace("VERSION",rawversion);
    }
}
