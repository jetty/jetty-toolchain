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

package org.eclipse.jetty.toolchain.test;

import java.lang.management.LockInfo;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * Java Runtime specific utility methods.
 */
public final class RuntimeUtil
{
    public static String generateThreadDump()
    {
        StringBuilder ret = new StringBuilder();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threads = threadMXBean.getThreadInfo(threadMXBean.getAllThreadIds(), 400);
        for (ThreadInfo thread : threads)
        {
            Thread.State state = thread.getThreadState();

            ret.append('"').append(thread.getThreadName()).append("\" ");
            ret.append(" #").append(thread.getThreadId());
            if (thread.isInNative())
                ret.append(" in-native");
            if (thread.isSuspended())
                ret.append(" suspended");
            if (state == Thread.State.BLOCKED)
            {
                if (thread.getBlockedTime() != (-1))
                {
                    ret.append(" blockedTime=").append(thread.getBlockedTime()).append("ms");
                }
                ret.append(" blockedCount=").append(thread.getBlockedCount());
            }
            if (state == Thread.State.WAITING || state == Thread.State.TIMED_WAITING)
            {
                if (thread.getWaitedTime() != (-1))
                {
                    ret.append(" waitedTime=").append(thread.getWaitedTime()).append("ms");
                }
                ret.append(" waitedCount=").append(thread.getWaitedCount());
            }

            ret.append("\n   java.lang.Thread.State: ").append(state.name());
            if (state == Thread.State.BLOCKED || state == Thread.State.TIMED_WAITING || state == Thread.State.WAITING)
            {
                // dump locks
                ret.append(" (locked on ").append(thread.getLockInfo()).append(')');
                if (thread.getLockOwnerId() != (-1))
                {
                    ret.append(" (owner=").append(thread.getLockOwnerName());
                    ret.append('/').append(thread.getLockOwnerId());
                    ret.append(')');
                }
                MonitorInfo[] monitors = thread.getLockedMonitors();
                if (monitors.length > 0)
                {
                    ret.append(" (monitors=");
                    joinToStrings(ret, "[", ",", "]", monitors);
                    ret.append(')');
                }
                LockInfo[] synchronizers = thread.getLockedSynchronizers();
                if (synchronizers.length > 0)
                {
                    ret.append(" (synchronizers=");
                    joinToStrings(ret, "[", ",", "]", synchronizers);
                    ret.append(')');
                }
            }

            final StackTraceElement[] stackTraceElements = thread.getStackTrace();
            for (final StackTraceElement stackTraceElement : stackTraceElements)
            {
                ret.append("\n\tat ");
                ret.append(stackTraceElement);
            }
            ret.append("\n\n");
        }
        return ret.toString();
    }

    private static void joinToStrings(StringBuilder ret, String prefixStr, String delimStr, String suffixStr, Object[] objects)
    {
        ret.append(prefixStr);
        boolean delim = false;
        if (objects != null)
        {
            for (Object obj : objects)
            {
                if (delim)
                {
                    ret.append(delimStr);
                }
                ret.append(obj);
                delim = true;
            }
        }
        ret.append(suffixStr);
    }
}
