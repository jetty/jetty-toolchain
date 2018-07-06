//
//  ========================================================================
//  Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.toolchain.perf;

import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Helper class that reports a number of information about the JVM and the OS,
 * useful to monitor the JVM during a benchmark.
 * <div>
 * Typical usage:
 * <pre>
 * PlatformMonitor monitor = new PlatformMonitor();
 * Start start = monitor.start();
 * if (start != null)
 * {
 *     // Monitoring started, print out
 *     // initial platform information.
 *     System.err.println(start);
 *
 *     // Perform benchmark
 *
 *     Stop stop = monitor.stop();
 *     if (stop != null)
 *     {
 *         // Monitoring stopped, print out
 *         // ending platform information.
 *         System.err.println(stop);
 *     }
 * }
 * </pre>
 * {@link PlatformMonitor} supports multiple concurrent invocations to
 * {@link #start()} and {@link #stop()}, so that it can be used on a
 * server when multiple clients sends to the server a "start monitor"
 * message. Calls to {@link #start()} and {@link #stop()} must be paired
 * and a {@link Start} object is returned for the first paired {@link #start()}
 * call, and a {@link Stop} object is returned for the last paired
 * {@link #stop()} call.
 * </div>
 * <div>
 * GC activity is being polled, by default every 250 ms, see
 * {@link #setMemoryPollInterval(long)}.
 * If the benchmark triggers GC at a higher rate, the results reported
 * by this class may be inaccurate.
 * </div>
 */
public class PlatformMonitor implements Runnable
{
    private static final MemoryUsage ZERO_MEMORY_USAGE = new MemoryUsage(0, 0, 0, 0);

    private final OperatingSystemMXBean operatingSystem;
    private final CompilationMXBean jitCompiler;
    private final MemoryMXBean heapMemory;
    private final AtomicInteger starts = new AtomicInteger();
    private final Supplier<MemoryUsage> edenMemoryPool;
    private final Supplier<MemoryUsage> survivorMemoryPool;
    private final Supplier<MemoryUsage> tenuredMemoryPool;
    private final GarbageCollector youngCollector;
    private final GarbageCollector oldCollector;
    private ScheduledFuture<?> memoryPoller;
    private ScheduledExecutorService scheduler;
    private long memoryPollInterval = 250;
    private long youngCount;
    private long youngTime;
    private long oldCount;
    private long oldTime;
    private long lastEden;
    private long lastSurvivor;
    private long lastTenured;
    private long time;
    private long cpuTime;
    private long jitTime;
    private Stop stop;

    public PlatformMonitor()
    {
        this.operatingSystem = ManagementFactory.getOperatingSystemMXBean();
        this.jitCompiler = ManagementFactory.getCompilationMXBean();
        this.heapMemory = ManagementFactory.getMemoryMXBean();

        List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();
        MemoryPoolMXBean emp = null;
        MemoryPoolMXBean smp = null;
        MemoryPoolMXBean omp = null;
        for (MemoryPoolMXBean memoryPool : memoryPools)
        {
            String memoryPoolName = memoryPool.getName();
            if ("PS Eden Space".equals(memoryPoolName) ||
                    "Par Eden Space".equals(memoryPoolName) ||
                    "G1 Eden Space".equals(memoryPoolName))
                emp = memoryPool;
            else if ("PS Survivor Space".equals(memoryPoolName) ||
                    "Par Survivor Space".equals(memoryPoolName) ||
                    "G1 Survivor Space".equals(memoryPoolName))
                smp = memoryPool;
            else if ("PS Old Gen".equals(memoryPoolName) ||
                    "CMS Old Gen".equals(memoryPoolName) ||
                    "G1 Old Gen".equals(memoryPoolName) ||
                    "Shenandoah".equals(memoryPoolName) ||
                    "ZHeap".equals(memoryPoolName))
                omp = memoryPool;
        }
        edenMemoryPool = emp == null ? () -> ZERO_MEMORY_USAGE : emp::getUsage;
        survivorMemoryPool = smp == null ? () -> ZERO_MEMORY_USAGE : smp::getUsage;
        tenuredMemoryPool = omp == null ? () -> ZERO_MEMORY_USAGE : omp::getUsage;

        List<GarbageCollectorMXBean> garbageCollectors = ManagementFactory.getGarbageCollectorMXBeans();
        GarbageCollectorMXBean yc = null;
        GarbageCollectorMXBean oc = null;
        for (GarbageCollectorMXBean garbageCollector : garbageCollectors)
        {
            String gcName = garbageCollector.getName();
            if ("PS Scavenge".equals(gcName) ||
                    "ParNew".equals(gcName) ||
                    "G1 Young Generation".equals(gcName) ||
                    "Shenandoah Pauses".equals(gcName))
                yc = garbageCollector;
            else if ("PS MarkSweep".equals(gcName) ||
                    "ConcurrentMarkSweep".equals(gcName) ||
                    "G1 Old Generation".equals(gcName) ||
                    "Shenandoah Cycles".equals(gcName) ||
                    "ZGC".equals(gcName))
                oc = garbageCollector;
        }
        youngCollector = yc == null ? GarbageCollector.NO_GARBAGE_COLLECTOR : GarbageCollector.from(yc);
        oldCollector = oc == null ? GarbageCollector.NO_GARBAGE_COLLECTOR : GarbageCollector.from(oc);
    }

    public long getMemoryPollInterval()
    {
        return memoryPollInterval;
    }

    public void setMemoryPollInterval(long gcPollInterval)
    {
        this.memoryPollInterval = gcPollInterval;
    }

    public void run()
    {
        long eden = edenMemoryPool.get().getUsed();
        long survivor = survivorMemoryPool.get().getUsed();
        long tenured = tenuredMemoryPool.get().getUsed();

        if (lastEden < eden)
            stop.edenBytes += eden - lastEden;
        if (lastSurvivor < survivor)
            stop.survivorBytes += survivor - lastSurvivor;
        if (lastTenured <= tenured)
            stop.tenuredBytes += tenured - lastTenured;

        lastEden = eden;
        lastSurvivor = survivor;
        lastTenured = tenured;
    }

    /**
     * Starts monitoring the platform.
     * <div>
     * This method returns a {@link Start} object the first time it is invoked,
     * or the first time it is invoked after paired {@link #stop()} calls.
     * For example:
     * <pre>
     * PlatformMonitor monitor = new PlatformMonitor();
     *
     * Start start = monitor.start(); // first start call, start != null
     * start = monitor.start();       // second start call, start == null
     * Stop stop = monitor.stop();    // first stop call, stop == null
     * stop = monitor.stop();         // last stop call, stop != null
     *
     * start = monitor.start();       // first start call after paired stops, start != null
     * stop = monitor.stop();         // paired stop call, stop != null
     * </pre>
     * </div>
     * @return a {@link Start} object if the monitoring started,
     * null for additional calls to this method not paired with
     * a {@link #stop()} call.
     * @see #stop()
     */
    public Start start()
    {
        synchronized (this)
        {
            if (starts.incrementAndGet() > 1)
                return null;

            Start start = new Start();
            stop = new Stop();

            System.gc();

            time = System.nanoTime();

            lastEden = edenMemoryPool.get().getUsed();
            lastSurvivor = survivorMemoryPool.get().getUsed();
            lastTenured = tenuredMemoryPool.get().getUsed();
            scheduler = Executors.newSingleThreadScheduledExecutor();
            memoryPoller = scheduler.scheduleWithFixedDelay(this, memoryPollInterval, memoryPollInterval, TimeUnit.MILLISECONDS);

            youngCount = youngCollector.getCollectionCount();
            youngTime = youngCollector.getCollectionTime();
            oldCount = oldCollector.getCollectionCount();
            oldTime = oldCollector.getCollectionTime();

            if (operatingSystem instanceof com.sun.management.OperatingSystemMXBean)
            {
                com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)operatingSystem;
                cpuTime = os.getProcessCpuTime();
            }

            jitTime = jitCompiler.getTotalCompilationTime();

            start.date = System.currentTimeMillis();
            start.os = String.format("%s %s %s", operatingSystem.getName(), operatingSystem.getVersion(), operatingSystem.getArch());
            start.cores = stop.cores = operatingSystem.getAvailableProcessors();
            start.jvm = String.format("%s %s %s %s", System.getProperty("java.vm.vendor"), System.getProperty("java.vm.name"), System.getProperty("java.vm.version"), System.getProperty("java.runtime.version"));
            if (operatingSystem instanceof com.sun.management.OperatingSystemMXBean)
            {
                com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)operatingSystem;
                start.totalMemory = os.getTotalPhysicalMemorySize();
                start.freeMemory = os.getFreePhysicalMemorySize();
            }
            start.heap = heapMemory.getHeapMemoryUsage();
            start.eden = edenMemoryPool.get();
            start.survivor = survivorMemoryPool.get();
            start.tenured = tenuredMemoryPool.get();

            return start;
        }
    }

    /**
     * Stops the monitoring of the platform.
     *
     * @return a {@link Stop} object if the monitoring stopped,
     * null for calls to this method not paired with
     * the first {@link #start()} call.
     * @see #start()
     */
    public Stop stop()
    {
        synchronized (this)
        {
            if (starts.decrementAndGet() > 0)
                return null;

            stop.date = System.currentTimeMillis();
            stop.time = System.nanoTime() - time;
            stop.jitTime = jitCompiler.getTotalCompilationTime() - jitTime;

            memoryPoller.cancel(false);
            scheduler.shutdown();

            stop.youngTime = youngCollector.getCollectionTime() - youngTime;
            stop.oldTime = oldCollector.getCollectionTime() - oldTime;
            stop.youngCount = youngCollector.getCollectionCount() - youngCount;
            stop.oldCount = oldCollector.getCollectionCount() - oldCount;

            if (operatingSystem instanceof com.sun.management.OperatingSystemMXBean)
            {
                com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean)operatingSystem;
                stop.cpuTime = os.getProcessCpuTime() - cpuTime;
            }
            else
            {
                stop.cpuTime = -1;
            }

            return stop;
        }
    }

    private static class Base
    {
        public String EOL = System.lineSeparator();

        public float percent(long dividend, long divisor)
        {
            if (divisor != 0)
                return (float)dividend * 100 / divisor;
            return Float.NaN;
        }

        public float mebiBytes(long bytes)
        {
            return (float)bytes / 1024 / 1024;
        }

        public float gibiBytes(long bytes)
        {
            return (float)bytes / 1024 / 1024 / 1024;
        }
    }

    public static class Start extends Base
    {
        public long date;
        public String os;
        public int cores;
        public String jvm;
        public long totalMemory;
        public long freeMemory;
        public MemoryUsage heap;
        public MemoryUsage eden;
        public MemoryUsage survivor;
        public MemoryUsage tenured;

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("========================================").append(EOL);
            builder.append("Monitoring Started at ").append(new Date(date)).append(EOL);
            builder.append("Operative System: ").append(os).append(EOL);
            builder.append("JVM: ").append(jvm).append(EOL);
            builder.append("Processors: ").append(cores).append(EOL);
            builder.append("System Memory: ").append(percent(totalMemory - freeMemory, totalMemory))
                    .append("% used of ").append(gibiBytes(totalMemory))
                    .append(" GiB").append(EOL);
            builder.append("Used Heap Size: ").append(mebiBytes(heap.getUsed()))
                    .append(" MiB").append(EOL);
            builder.append("Max Heap Size: ").append(mebiBytes(heap.getMax()))
                    .append(" MiB").append(EOL);
            builder.append("Young Generation Heap Size: ").append(mebiBytes(heap.getMax() - tenured.getMax()))
                    .append(" MiB").append(EOL);
            builder.append("- - - - - - - - - - - - - - - - - - - - ");
            return builder.toString();
        }
    }

    public static class Stop extends Base
    {
        private int cores;
        public long date;
        public long time;
        public long jitTime;
        public long youngTime;
        public long youngCount;
        public long oldTime;
        public long oldCount;
        public long edenBytes;
        public long survivorBytes;
        public long tenuredBytes;
        public long cpuTime;

        @Override
        public String toString()
        {
            StringBuilder builder = new StringBuilder();
            builder.append("- - - - - - - - - - - - - - - - - - - - ").append(EOL);
            builder.append("Monitoring Ended at ").append(new Date(date)).append(EOL);
            builder.append("Elapsed Time: ").append(TimeUnit.NANOSECONDS.toMillis(time)).append(" ms").append(EOL);
            builder.append("\tTime in JIT Compilation: ").append(jitTime).append(" ms").append(EOL);
            builder.append("\tTime in Young GC: ").append(youngTime).append(" ms (")
                    .append(youngCount).append(" collections)").append(EOL);
            builder.append("\tTime in Old GC: ").append(oldTime).append(" ms (")
                    .append(oldCount).append(" collections)").append(EOL);
            builder.append("Garbage Generated in Eden Space: ").append(mebiBytes(edenBytes))
                    .append(" MiB").append(EOL);
            builder.append("Garbage Generated in Survivor Space: ").append(mebiBytes(survivorBytes))
                    .append(" MiB").append(EOL);
            builder.append("Garbage Generated in Tenured Space: ").append(mebiBytes(tenuredBytes))
                    .append(" MiB").append(EOL);
            builder.append("Average CPU Load: ").append(percent(cpuTime, time)).append("/")
                    .append(100 * cores).append(EOL);
            builder.append("========================================");
            return builder.toString();
        }
    }

    private interface GarbageCollector
    {
        static final GarbageCollector NO_GARBAGE_COLLECTOR = new GarbageCollector()
        {
        };

        static GarbageCollector from(GarbageCollectorMXBean mxBean)
        {
            return new GarbageCollector()
            {
                @Override
                public long getCollectionCount()
                {
                    return mxBean.getCollectionCount();
                }

                @Override
                public long getCollectionTime()
                {
                    return mxBean.getCollectionTime();
                }
            };
        }

        /**
         * @return the total number of collections that have occurred.
         */
        public default long getCollectionCount()
        {
            return 0;
        }

        /**
         * @return the approximate accumulated collection elapsed time in milliseconds.
         */
        public default long getCollectionTime()
        {
            return 0;
        }
    }
}
