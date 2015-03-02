//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
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

public class JDK
{
    public static final boolean IS_5 = isJavaVersionAtLeast(1,5);
    public static final boolean IS_6 = isJavaVersionAtLeast(1,6);
    public static final boolean IS_7 = isJavaVersionAtLeast(1,7);
    public static final boolean IS_8 = isJavaVersionAtLeast(1,8);
    public static final boolean IS_9 = isJavaVersionAtLeast(1,9);

    private static boolean isJavaVersionAtLeast(int maj, int min)
    {
        String jver = System.getProperty("java.version");
        if (jver == null)
        {
            System.err.println("## ERROR: System.getProperty('java.version') == null !?");
            return false;
        }
        String vparts[] = jver.split("\\.");
        if (vparts.length < 2)
        {
            System.err.println("## ERROR: Invalid java version format '" + jver + "'");
            return false;
        }
        return (toInt(vparts[0]) >= maj && toInt(vparts[1]) >= min);
    }

    private static int toInt(String val)
    {
        try
        {
            return Integer.parseInt(val);
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }
}
