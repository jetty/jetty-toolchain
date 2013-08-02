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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the multi-threaded event queue test utility.
 * <p>
 * This test can be a little confusing, as there are timings (timeouts, delays, etc).
 * <p>
 * The timings found in the test.. 1) The ScheduledFuture delay on starting to wait for event count 2) The ScheduledFuture delay on starting to generate events
 * 3) The timeout on waiting for the event count (used by the EventQueue implementation) 4) The timeout on waiting for the test assertions to be satisfied
 */
public class EventQueueTest
{
    public class EventAdder implements Callable<Void>
    {
        public String[] events;

        public EventAdder(String... events)
        {
            this.events = events;
        }

        public Void call() throws Exception
        {
            for (String event : events)
            {
                queue.add(event);
            }
            return null;
        }
    }

    public class EventGen implements Callable<Void>
    {
        private final int totalEvents;
        private final int delayDuration;
        private final TimeUnit delayUnit;

        public EventGen(int eventCount, int delayBetweenDuration, TimeUnit delayBetweenUnit)
        {
            this.totalEvents = eventCount;
            this.delayDuration = delayBetweenDuration;
            this.delayUnit = delayBetweenUnit;
        }

        public Void call() throws Exception
        {
            for (int i = 0; i < totalEvents; i++)
            {
                queue.add(String.format("evt-%d",i));
                delayUnit.sleep(delayDuration);
            }
            return null;
        }
    }

    public class ExpectedEventCount implements Callable<Void>
    {
        private final int expectedCount;
        private final int waitDuration;
        private final TimeUnit waitUnit;

        public ExpectedEventCount(int expectedCount, int waitDuration, TimeUnit waitUnit)
        {
            this.expectedCount = expectedCount;
            this.waitDuration = waitDuration;
            this.waitUnit = waitUnit;
        }

        public Void call() throws Exception
        {
            queue.awaitEventCount(expectedCount,waitDuration,waitUnit);
            return null;
        }
    }

    private EventQueue<String> queue;
    private ScheduledExecutorService executor;

    private void assertFinishedWithoutError(Future<Void> future, int timeout, TimeUnit unit) throws Throwable
    {
        try
        {
            future.get(timeout,unit);
        }
        catch (ExecutionException e)
        {
            Throwable cause = e.getCause();
            if (cause != null)
            {
                throw cause;
            }
            throw e;
        }
    }

    @Before
    public void init()
    {
        queue = new EventQueue<String>();
        executor = Executors.newScheduledThreadPool(5);
    }

    @After
    public void shutdown()
    {
        queue.shutdown();
        executor.shutdownNow();
    }

    @Test
    public void testNormalWait() throws Throwable
    {
        String events[] = new String[]
        { "Started", "Finished" };
        Callable<Void> eventsTask = new EventAdder(events);
        int delayDuration = 500;
        TimeUnit delayUnit = TimeUnit.MILLISECONDS;

        // Add events after a short delay
        executor.schedule(eventsTask,delayDuration,delayUnit);

        // Await Events
        queue.awaitEventCount(events.length,1,TimeUnit.SECONDS);
    }

    @Test
    public void testEventsOccurBeforeWait() throws Throwable
    {
        int expectedCount = 3;
        Callable<Void> awaitTask = new ExpectedEventCount(expectedCount,3,TimeUnit.SECONDS);
        Callable<Void> eventsTask = new EventAdder("Processing","Finished");

        // One event occurs before we start waiting
        queue.add("Started");

        // Start waiting for expected events
        Future<Void> future = executor.submit(awaitTask);

        // Create More Events
        executor.schedule(eventsTask,500,TimeUnit.MILLISECONDS);

        // Assert that it worked
        assertFinishedWithoutError(future,4,TimeUnit.SECONDS);
    }

    @Test
    public void testLotsOfEvents() throws Throwable
    {
        int expectedCount = 100;
        Callable<Void> eventGen = new EventGen(expectedCount,20,TimeUnit.MILLISECONDS);
        Callable<Void> awaitTask = new ExpectedEventCount(expectedCount,3,TimeUnit.SECONDS);

        // Start Generating Events
        executor.submit(eventGen);
        // Start waiting for expected events
        Future<Void> future = executor.schedule(awaitTask,500,TimeUnit.MILLISECONDS);

        // Assert that it worked
        assertFinishedWithoutError(future,4,TimeUnit.SECONDS);
    }
}
