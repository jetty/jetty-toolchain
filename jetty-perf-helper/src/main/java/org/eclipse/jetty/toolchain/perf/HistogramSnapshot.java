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

import java.util.concurrent.TimeUnit;
import org.HdrHistogram.Histogram;
import org.HdrHistogram.HistogramIterationValue;

public class HistogramSnapshot implements MeasureConverter
{
    private final Histogram histogram;
    private final long buckets;
    private final String name;
    private final String unit;
    private final MeasureConverter converter;

    public HistogramSnapshot(Histogram histogram)
    {
        this(histogram, 32);
    }

    public HistogramSnapshot(Histogram histogram, long buckets)
    {
        this(histogram, buckets, "Measures", "ms", null);
    }

    public HistogramSnapshot(Histogram histogram, long buckets, String name, String unit, MeasureConverter converter)
    {
        this.histogram = histogram;
        this.buckets = buckets;
        this.name = name;
        this.unit = unit;
        this.converter = converter == null ? this : converter;
    }

    @Override
    public long convert(long measure)
    {
        return TimeUnit.NANOSECONDS.toMillis(measure);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        long range = histogram.getMaxValue();

        long maxBucketCount = 0;
        for (HistogramIterationValue value : histogram.linearBucketValues(range / buckets))
        {
            long bucketCount = value.getCountAddedInThisIterationStep();
            if (bucketCount > maxBucketCount)
                maxBucketCount = bucketCount;
        }

        double previousPercentile = 0;
        for (HistogramIterationValue value : histogram.linearBucketValues(range / buckets))
        {
            long bucketCount = value.getCountAddedInThisIterationStep();

            // Draw the ASCII "gaussian" point.
            long point = maxBucketCount == 0 ? 0 : Math.round((double)bucketCount / maxBucketCount * buckets);
            if (point == buckets)
                --point;
            for (long j = 0; j < point; ++j)
                builder.append(" ");
            builder.append("@");
            for (long j = point + 1; j < buckets; ++j)
                builder.append(" ");

            // Print the measure and its frequency.
            builder.append("  _  ");
            builder.append(String.format("%,d %s (%d, %.2f%%)",
                    converter.convert(value.getValueIteratedTo()),
                    unit,
                    bucketCount,
                    100D * bucketCount / histogram.getTotalCount()));
            double percentile = value.getPercentile();
            if (previousPercentile < 50D && percentile >= 50D)
                builder.append(" ^50%");
            if (previousPercentile < 85D && percentile >= 85D)
                builder.append(" ^85%");
            if (previousPercentile < 95D && percentile >= 95D)
                builder.append(" ^95%");
            if (previousPercentile < 99D && percentile >= 99D)
                builder.append(" ^99%");
            if (previousPercentile < 99.9D && percentile >= 99.9D)
                builder.append(" ^99.9%");
            previousPercentile = percentile;
            builder.append(System.lineSeparator());
        }

        builder.append(String.format("%s: %d samples | min/avg/50th%%/99th%%/max = %,d/%,d/%,d/%,d/%,d %s",
                name,
                histogram.getTotalCount(),
                converter.convert(histogram.getMinValue()),
                converter.convert(Math.round(histogram.getMean())),
                converter.convert(histogram.getValueAtPercentile(50D)),
                converter.convert(histogram.getValueAtPercentile(99D)),
                converter.convert(histogram.getMaxValue()),
                unit));

        return builder.toString();
    }
}
