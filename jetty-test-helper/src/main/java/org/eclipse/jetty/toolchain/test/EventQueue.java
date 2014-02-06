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

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Event Queue for capturing potential events within a testing scenario.
 * 
 * @param <E>
 */
@SuppressWarnings("serial")
public class EventQueue<E> extends LinkedBlockingQueue<E>
{
    public static final boolean DEBUG = false;
    private static final long DEBUG_START = System.currentTimeMillis();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition countReached = lock.newCondition();
    private int goalCount = Integer.MAX_VALUE;

    @Override
    public boolean add(E o)
    {
        debug("add(%s)",o);
        lock.lock();
        try
        {
            boolean ret = super.add(o);
            debug("added: %s",o);
            goalCheck();
            return ret;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void awaitEventCount(int expectedEventCount, int timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException
    {
        debug("awaitEventCount(%d,%d,%s)",expectedEventCount,timeoutDuration,timeoutUnit);
        lock.lock();
        try
        {
            goalCount = expectedEventCount;
            if (goalCheck())
                return;
            debug("awaiting countReached");
            if (!countReached.await(timeoutDuration,timeoutUnit))
            {
                throw new TimeoutException(String.format("Timeout waiting for %d events (found %d)",expectedEventCount,size()));
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    private void debug(String format, Object... args)
    {
        if (DEBUG)
        {
            StringBuilder fmt2 = new StringBuilder();
            fmt2.append(String.format("%,6d [EventQueue|",System.currentTimeMillis() - DEBUG_START));
            fmt2.append(Thread.currentThread().getName());
            fmt2.append("] ").append(String.format(format,args));
            System.err.println(fmt2);
        }
    }

    private boolean goalCheck()
    {
        if (size() >= goalCount)
        {
            countReached.signalAll();
            return true;
        }
        return false;
    }

    @Override
    public boolean offer(E o)
    {
        debug("offer(%s)",o);
        lock.lock();
        try
        {
            boolean ret = super.offer(o);
            debug("offered: %s",o);
            goalCheck();
            return ret;
        }
        finally
        {
            lock.unlock();
        }
    }

    public void shutdown()
    {
        // TODO Auto-generated method stub
    }
}
