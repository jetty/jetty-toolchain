package org.eclipse.jetty.toolchain.version;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class ReleaseTest
{
    private void assertParseReleasedOn(String expected, String rawdate)
    {
        Release release = new Release();
        release.parseReleasedOn(0, rawdate);
        Date actualDate = release.getReleasedOn();
        if (rawdate == null)
        {
            Assert.assertNull("Was expecting a null released-on date for a null rawdate",actualDate);
        }
        else
        {
            Assert.assertNotNull("released-on date should not be null",actualDate);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); // ISO 8601 format
            String actual = formatter.format(actualDate);
            Assert.assertEquals("Parsing of ReleasedOn of [" + rawdate + "]",expected,actual);
        }
    }

    @Test
    public void testParseReleasedOn_MessyMonthYear()
    {
        String rawdate = " - May 1998"; // Month Year
        String expected = "1998-05-01";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_MonthYear()
    {
        String rawdate = "June 1998"; // Month Year
        String expected = "1998-06-01";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_OrdinalStyle1()
    {
        String rawdate = "July 7th 2011";
        String expected = "2011-07-07";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_OrdinalStyle2()
    {
        String rawdate = "3rd June 2000";
        String expected = "2000-06-03";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_OrdinalStyle3()
    {
        String rawdate = "21st Aug 2000";
        String expected = "2000-08-21";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_OrdinalStyle4()
    {
        String rawdate = "16th Aug 2000";
        String expected = "2000-08-16";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_OrdinalStyleBad()
    {
        String rawdate = "24st Aug 2000";
        String expected = "2000-08-24";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_UsaStyle1()
    {
        String rawdate = "5 May 1998"; // USA Format
        String expected = "1998-05-05";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_UsaStyle2()
    {
        String rawdate = "Wed 8 April 1998"; // USA Format w/Weekday
        String expected = "1998-04-08";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_UsaStyle3()
    {
        String rawdate = "Sun 15 Mar 1998"; // USA Format w/Weekday
        String expected = "1998-03-15";
        assertParseReleasedOn(expected,rawdate);
    }

    @Test
    public void testParseReleasedOn_UsaStyle4()
    {
        String rawdate = "29 Sep 1998"; // USA Format
        String expected = "1998-09-29";
        assertParseReleasedOn(expected,rawdate);
    }
}
