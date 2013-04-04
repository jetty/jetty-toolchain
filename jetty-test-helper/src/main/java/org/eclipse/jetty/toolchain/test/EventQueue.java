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

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Event Queue for capturing potential events within a testing scenario.
 * 
 * @param <E>
 */
@SuppressWarnings("serial")
public class EventQueue<E> extends LinkedBlockingQueue<E>
{
    private final ReentrantLock lock = new ReentrantLock();
    private AtomicReference<CountDownLatch> expectedEventCountLatch = new AtomicReference<CountDownLatch>();

    @Override
    public boolean add(E o)
    {
        boolean ret = super.add(o);
        triggerCountdown();
        return ret;
    }

    public void awaitEventCount(int expectedEventCount, int timeoutDuration, TimeUnit timeoutUnit) throws TimeoutException, InterruptedException
    {
        lock.lock(); // wait until any active awaitEventCount() calls to complete
        try
        {
            CountDownLatch latch = new CountDownLatch(expectedEventCount);
            expectedEventCountLatch.set(latch);

            if (!latch.await(timeoutDuration,timeoutUnit))
            {
                throw new TimeoutException(String.format("Timeout waiting for %d events",expectedEventCount));
            }
        }
        finally
        {
            lock.unlock();
        }
    }

    @Override
    public boolean offer(E o)
    {
        boolean ret = super.offer(o);
        triggerCountdown();
        return ret;
    }

    private void triggerCountdown()
    {
        synchronized (this)
        {
            CountDownLatch countdown = expectedEventCountLatch.get();
            if (countdown != null)
            {
                countdown.countDown();
            }
        }
    }
}
