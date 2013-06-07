//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.toolchain.test;

/**
 * Collection of utility methods for manipulating Strings for zen purposes.
 */
public final class StringMangler
{
    /**
     * Condenses a classname by stripping down the package name to just the first character of each package name
     * segment.
     * <p>
     * 
     * <pre>
     * Examples:
     * "org.eclipse.jetty.test.FooTest"           = "oejt.FooTest"
     * "org.eclipse.jetty.server.logging.LogTest" = "oejsl.LogTest"
     * </pre>
     * 
     * @param classname
     *            the fully qualified class name
     * @return the condensed name
     */
    public static String condensePackageString(String classname)
    {
        String parts[] = classname.split("\\.");
        StringBuilder dense = new StringBuilder();
        for (int i = 0; i < (parts.length - 1); i++)
        {
            dense.append(parts[i].charAt(0));
        }
        dense.append('.').append(parts[parts.length - 1]);
        return dense.toString();
    }
    
    /**
     * Smash a long string to fit within the max string length, by taking the middle section of the string and replacing them with an ellipsis "..."
     * <p>
     * 
     * <pre>
     * Examples:
     * .maxStringLength( 9, "Eatagramovabits") == "Eat...its"
     * .maxStringLength(10, "Eatagramovabits") == "Eat...bits"
     * .maxStringLength(11, "Eatagramovabits") == "Eata...bits"
     * </pre>
     * 
     * @param max
     *            the maximum size of the string (minimum size supported is 9)
     * @param raw
     *            the raw string to smash
     * @return the ellipsis'd version of the string.
     */
    public static String maxStringLength(int max, String raw)
    {
        int length = raw.length();
        if (length <= max)
        {
            // already short enough
            return raw;
        }

        if (max < 9)
        {
            // minimum supported
            return raw.substring(0,max);
        }

        StringBuilder ret = new StringBuilder();
        int startLen = (int)Math.round((double)max / (double)3);
        ret.append(raw.substring(0,startLen));
        ret.append("...");
        ret.append(raw.substring(length - (max - startLen - 3)));

        return ret.toString();
    }
}
