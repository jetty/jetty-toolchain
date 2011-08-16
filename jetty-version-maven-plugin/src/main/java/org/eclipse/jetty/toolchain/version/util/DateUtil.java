package org.eclipse.jetty.toolchain.version.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil
{
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static SimpleDateFormat createIso8601Format()
    {
        // 2011-07-27 11:16:03 +1000
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    }

    public static SimpleDateFormat createRfc2822Format()
    {
        // Wed Jul 27 11:16:03 2011 +100
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
    }

    /**
     * Format a date to in ISO 8601 format (as used by GIT) adjusted to UTC timezone.
     */
    public static String formatIso8601(Date date)
    {
        SimpleDateFormat formatter = createIso8601Format();
        formatter.setTimeZone(UTC);
        return formatter.format(date);
    }

    /**
     * Format a date to in RFC 2822 format (as used by GIT) adjusted to UTC timezone.
     */
    public static String formatRfc2822(Date date)
    {
        SimpleDateFormat formatter = createRfc2822Format();
        formatter.setTimeZone(UTC);
        return formatter.format(date);
    }

    public static Date parseIso8601(String rawstr) throws ParseException
    {
        return createIso8601Format().parse(rawstr);
    }
}
