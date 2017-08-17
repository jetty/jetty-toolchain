//
//  ========================================================================
//  Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
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
 * Common Java JVM/JDK environment utilities
 */
public class JDK
{
    /**
     * True if JDK is 1.5 (or newer) 
     */
    public static final boolean IS_5 = isJavaVersionAtLeast(1,5);
    /**
     * True if JDK is 1.6 (or newer) 
     */
    public static final boolean IS_6 = isJavaVersionAtLeast(1,6);
    /**
     * True if JDK is 1.7 (or newer) 
     */
    public static final boolean IS_7 = isJavaVersionAtLeast(1,7);
    /**
     * True if JDK is 1.8 (or newer) 
     */
    public static final boolean IS_8 = isJavaVersionAtLeast(1,8);
    /**
     * True if JDK is 9.0 (or newer)
     */
    public static final boolean IS_9 = isJavaVersionAtLeast(9,0);

    private static boolean isJavaVersionAtLeast(int maj, int min)
    {
        String jvmSpecVer = System.getProperty("java.vm.specification.version");
        if (jvmSpecVer == null)
        {
            System.err.println("## ERROR: System.getProperty('java.vm.specification.version') == null !?");
            return false;
        }

        String versionParts[] = jvmSpecVer.split("[-.]");
        int actualMaj = 0;
        int actualMin = 0;

        if (versionParts.length > 0)
        {
            actualMaj = toInt(versionParts[0]);
            if(versionParts.length > 1)
            {
                actualMin = toInt(versionParts[1]);
            }
        }
        return actualMaj > maj || (actualMaj == maj && actualMin >= min);
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
