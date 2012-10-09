//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
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

import java.io.File;

/**
 * Some simple OS specific utilities.
 */
public final class OS
{
    public static final String OS_NAME = System.getProperty("os.name");
    public static final boolean IS_WINDOWS = isOSName("Windows");
    public static final boolean IS_OSX = isOSName("Mac OS X");
    public static final boolean IS_LINUX = isOSName("Linux") || isOSName("LINUX");
    public static final boolean IS_UNIX = isOSName("Unix") || isOSName("AIX") || IS_LINUX || IS_OSX;
    public static final String LN = System.getProperty("line.separator");
    
    private OS() {
        /* prevent instantiation */
    }

    /**
     * Convert path separators to the System path separators.
     * <p>
     * This helps ensure that the paths provided in the unit tests work equally as well on unix / osx / windows.
     * 
     * @param path
     *            the raw path to convert
     * @return the converted path
     */
    public static String separators(String path)
    {
        StringBuilder ret = new StringBuilder();
        for (char c : path.toCharArray())
        {
            if ((c == '/') || (c == '\\'))
            {
                ret.append(File.separatorChar);
            }
            else
            {
                ret.append(c);
            }
        }
        return ret.toString();
    }

    /**
     * Simple test for OS Name
     * 
     * @param name
     *            the name to look for
     * @return true if the name is found in the system OS name.
     */
    private static boolean isOSName(String name)
    {
        if (OS_NAME == null || name == null)
        {
            return false;
        }
        return OS_NAME.contains(name);
    }
}
