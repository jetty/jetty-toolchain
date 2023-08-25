//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.setuid;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * Class is for changing user and groupId, it can also be use to retrieve user information by using getpwuid(uid) or getpwnam(username) of both linux and unix
 * systems
 */
public interface LibC extends Library
{
    LibC INSTANCE = Native.load("c", LibC.class);

    int setumask(int mask);

    int setuid(int uid);

    int setgid(int gid);

    int setgroups(int[] gids);

    Passwd getpwnam(String name);

    Passwd getpwuid(int uid);

    Group getgrnam(String name);

    Group getgrgid(int gid);

    int getrlimit(int resource, RLimit rLimit);

    int setrlimit(int resource, RLimit rlimit);

    /**
     * Compile and run the following C program to get the <code>RLIMIT_NOFILE</code> value of you OS of choice.
     * <pre>
     * #include &lt;stdio.h&gt;
     * #include &lt;sys/resource.h&gt;
     *
     * int main()
     * {
     *   printf("RLIMIT_NOFILE = %d\n", RLIMIT_NOFILE);
     *   return 0;
     * }
     * </pre>
     */
    class Constants
    {
        public static final int RLIMIT_NOFILE;
        static
        {
            if (Platform.isMac())
                RLIMIT_NOFILE = 8;
            else if (Platform.isSolaris())
                RLIMIT_NOFILE = 5;
            else if (Platform.isAIX())
                RLIMIT_NOFILE = 6;
            else if (Platform.isLinux())
                RLIMIT_NOFILE = 7;
            else
                RLIMIT_NOFILE = Integer.MIN_VALUE;
        }
    }
}
