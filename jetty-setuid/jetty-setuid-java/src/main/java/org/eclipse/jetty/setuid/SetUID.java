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

package org.eclipse.jetty.setuid;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * Class is for changing user and groupId, it can also be use to retrieve user information by using getpwuid(uid) or getpwnam(username) of both linux and unix
 * systems
 */

public class SetUID
{
    private static final Logger LOG = Log.getLogger(SetUID.class);

    public static final String __FILENAME = "libsetuid";

    public static final int OK = 0;
    public static final int ERROR = -1;

    public static native int setumask(int mask);

    public static native int setuid(int uid);

    public static native int setgid(int gid);

    public static native int setgroups(int[] gids);

    public static native Passwd getpwnam(String name) throws SecurityException;

    public static native Passwd getpwuid(int uid) throws SecurityException;

    public static native Group getgrnam(String name) throws SecurityException;

    public static native Group getgrgid(int gid) throws SecurityException;

    public static native RLimit getrlimitnofiles();

    public static native int setrlimitnofiles(RLimit rlimit);

    private static class LibFilenameFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            if (name.toLowerCase().contains(__FILENAME))
                return true;

            return false;
        }
    }

    private static void loadLibrary()
    {
        // try loading file from ${jetty.libsetuid.path}
        try
        {
            if (System.getProperty("jetty.libsetuid.path") != null)
            {
                LOG.debug("System.loadLibrary(jetty.libsetuid.path)");

                File lib = new File(System.getProperty("jetty.libsetuid.path"));
                if (lib.exists())
                {
                    System.load(lib.getCanonicalPath());
                }
                return;
            }

        }
        catch (Throwable e)
        {
            // Ignorable if there is another way to find the lib
            LOG.debug(e);
        }

        // try loading using the platform native library name mapping
        try
        {
            LOG.debug("System.loadLibrary(setuid)");
            System.loadLibrary("setuid");
            return;
        }
        catch (Throwable e)
        {
            // Ignorable if there is another way to find the lib
            LOG.debug(e);
        }

        // try loading using well-known path
        try
        {
            if (System.getProperty("jetty.home") != null)
            {
                File lib = getLib(new File(System.getProperty("jetty.home"),"lib/setuid/"));

                if (lib != null && lib.exists())
                {
                    LOG.debug("System.load((jetty.home)lib.getCanonicalPath()");

                    System.load(lib.getCanonicalPath());
                }
                return;
            }

        }
        catch (Throwable e)
        {
            LOG.debug(e);
        }

        // try to load from jetty.lib where rpm puts this file
        try
        {
            if (System.getProperty("jetty.lib") != null)
            {
                File lib = getLib(new File(System.getProperty("jetty.lib")));

                LOG.debug("looking for {}",lib.getAbsolutePath());
                if (lib != null && lib.exists())
                {
                    LOG.debug("System.load((jetty.lib)lib.getCanonicalPath())");
                    System.load(lib.getCanonicalPath());
                }
                return;
            }

        }
        catch (Throwable e)
        {
            LOG.debug(e);
        }

        LOG.warn("Error: libsetuid.so could not be found");
    }

    private static File getLib(File dir)
    {
        File[] files = dir.listFiles(new LibFilenameFilter());
        if (files == null || files.length == 0)
            return null;

        File file = null;
        String osName = getOSName();

        for (File f : files)
        {
            if (f.getName().endsWith(Server.getVersion() + ".so"))
            {
                file = f;
                break;
            }

            if (f.getName().endsWith(osName + ".so"))
            {
                LOG.debug("OS specific file found: {}",f.getName());

                file = f;
                break;
            }
        }

        if (file == null)
        {
            file = files[0]; // couldn't get a match on version number, just pick first

            LOG.debug("Defaulting to first file: {}",file.getName());

        }

        LOG.debug("setuid library {}",file.getName());

        return file;
    }

    private static File getLib(String dir)
    {
        return getLib(new File(dir));
    }

    private static String getOSName()
    {
        String os = System.getProperty("os.name");

        if ("Mac OS X".equals(os))
        {
            return "osx";
        }
        else if ("Linux".equals(os) || "LINUX".equals(os))
        {
            return "linux";
        }

        return null;
    }

    static
    {
        loadLibrary();
    }

}
