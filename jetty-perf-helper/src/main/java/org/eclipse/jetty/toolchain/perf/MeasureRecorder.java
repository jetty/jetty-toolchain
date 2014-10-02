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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class MeasureRecorder
{
    private final AtomicLong count = new AtomicLong();
    private final AtomicLong min = new AtomicLong();
    private final AtomicLong max = new AtomicLong();
    private final AtomicLong total = new AtomicLong();
    private final ConcurrentMap<Long, AtomicLong> measures = new ConcurrentHashMap<>();
    private final Converter converter;
    private final String name;
    private final String unit;

    public MeasureRecorder(Converter converter, String name, String unit)
    {
        this.converter = converter;
        this.name = name;
        this.unit = unit;
    }

    public void reset()
    {
        count.set(0);
        min.set(Long.MAX_VALUE);
        max.set(Long.MIN_VALUE);
        total.set(0);
        measures.clear();
    }

    public void record(long measure, boolean distribution)
    {
        count.incrementAndGet();
        updateMin(min, measure);
        updateMax(max, measure);
        total.addAndGet(measure);

        if (distribution)
        {
            AtomicLong count = measures.get(measure);
            if (count == null)
            {
                count = new AtomicLong();
                AtomicLong existing = measures.putIfAbsent(measure, count);
                if (existing != null)
                    count = existing;
            }
            count.incrementAndGet();
        }
    }

    public Snapshot snapshot()
    {
        Map<Long, Long> copy = new TreeMap<>();
        for (Map.Entry<Long, AtomicLong> entry : measures.entrySet())
            copy.put(entry.getKey(), entry.getValue().get());
        return new Snapshot(count.get(), min.get(), max.get(), total.get(), copy);
    }

    public static void updateMin(AtomicLong currentMin, long newValue)
    {
        long oldValue = currentMin.get();
        while (newValue < oldValue)
        {
            if (currentMin.compareAndSet(oldValue, newValue))
                break;
            oldValue = currentMin.get();
        }
    }

    public static void updateMax(AtomicLong currentMax, long newValue)
    {
        long oldValue = currentMax.get();
        while (newValue > oldValue)
        {
            if (currentMax.compareAndSet(oldValue, newValue))
                break;
            oldValue = currentMax.get();
        }
    }

    public class Snapshot
    {
        public final long count;
        public final long min;
        public final long max;
        public final long total;
        public final Map<Long, Long> measures;

        private Snapshot(long count, long min, long max, long total, Map<Long, Long> measures)
        {
            this.count = count;
            this.min = min;
            this.max = max;
            this.total = total;
            this.measures = measures;
        }

        @Override
        public String toString()
        {
            String eol = System.lineSeparator();
            StringBuilder builder = new StringBuilder();

            long average = 0;
            long measureAt50thPercentile = 0;
            long measureAt99thPercentile = 0;

            if (count == 1)
            {
                average = total;
                measureAt50thPercentile = min;
                measureAt99thPercentile = max;
            }
            else if (count > 1)
            {
                average = total / count;
                long samples = 0;
                long maxLatencyBucketFrequency = 0;
                long previousMeasure = 0;
                long[] measureBucketFrequencies = new long[20];
                long minMeasure = min;
                long measureRange = max - minMeasure;
                for (Iterator<Map.Entry<Long, Long>> entries = measures.entrySet().iterator(); entries.hasNext();)
                {
                    Map.Entry<Long, Long> entry = entries.next();
                    long latency = entry.getKey();
                    Long bucketIndex = measureRange == 0 ? 0 : (latency - minMeasure) * measureBucketFrequencies.length / measureRange;
                    int index = bucketIndex.intValue() == measureBucketFrequencies.length ? measureBucketFrequencies.length - 1 : bucketIndex.intValue();
                    long value = entry.getValue();
                    samples += value;
                    measureBucketFrequencies[index] += value;
                    if (measureBucketFrequencies[index] > maxLatencyBucketFrequency)
                        maxLatencyBucketFrequency = measureBucketFrequencies[index];
                    if (measureAt50thPercentile == 0 && samples > count / 2)
                        measureAt50thPercentile = (previousMeasure + latency) / 2;
                    if (measureAt99thPercentile == 0 && samples > count - count / 100)
                        measureAt99thPercentile = (previousMeasure + latency) / 2;
                    previousMeasure = latency;
                    entries.remove();
                }

                builder.append(name).append(" - distribution curve (x axis: frequency, y axis: ").append(name).append("):").append(eol);
                double percentile = 0.0;
                for (int i = 0; i < measureBucketFrequencies.length; ++i)
                {
                    long latencyBucketFrequency = measureBucketFrequencies[i];
                    int value = maxLatencyBucketFrequency == 0 ? 0 : Math.round(latencyBucketFrequency * (float)measureBucketFrequencies.length / maxLatencyBucketFrequency);
                    if (value == measureBucketFrequencies.length)
                        value = value - 1;
                    for (int j = 0; j < value; ++j)
                        builder.append(" ");
                    builder.append("@");
                    for (int j = value + 1; j < measureBucketFrequencies.length; ++j)
                        builder.append(" ");
                    builder.append("  _  ");
                    double percentage = 100D * latencyBucketFrequency / samples;
                    builder.append(converter.convert((measureRange * (i + 1) / measureBucketFrequencies.length) + minMeasure));
                    builder.append(String.format(" %s (%d, %.2f%%)", unit, latencyBucketFrequency, percentage));
                    double last = percentile;
                    percentile += percentage;
                    if (last < 50.0 && percentile >= 50.0)
                        builder.append(" ^50%");
                    if (last < 85.0 && percentile >= 85.0)
                        builder.append(" ^85%");
                    if (last < 95.0 && percentile >= 95.0)
                        builder.append(" ^95%");
                    if (last < 99.0 && percentile >= 99.0)
                        builder.append(" ^99%");
                    if (last < 99.9 && percentile >= 99.9)
                        builder.append(" ^99.9%");
                    builder.append(eol);
                }
            }

            builder.append(String.format("%s - %d samples | min/avg/50th%%/99th%%/max = %d/%d/%d/%d/%d %s",
                    name,
                    count,
                    converter.convert(min),
                    converter.convert(average),
                    converter.convert(measureAt50thPercentile),
                    converter.convert(measureAt99thPercentile),
                    converter.convert(max),
                    unit));

            return builder.toString();
        }
    }

    public interface Converter
    {
        public long convert(long measure);
    }
}
