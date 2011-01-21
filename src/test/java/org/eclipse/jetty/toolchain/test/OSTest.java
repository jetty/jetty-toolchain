package org.eclipse.jetty.toolchain.test;

import junit.framework.Assert;

import org.junit.Test;

public class OSTest
{
    @Test
    public void testSeparators()
    {
        if (OS.IS_WINDOWS)
        {
            Assert.assertEquals("target\\tests\\test-Foo",OS.separators("target/tests/tests-Foo"));
            Assert.assertEquals("target\\tests\\test-Foo",OS.separators("target/tests\\tests-Foo"));
        }
        else
        {
            Assert.assertEquals("target/tests/tests-Foo",OS.separators("target/tests/tests-Foo"));
            Assert.assertEquals("target/tests/tests-Foo",OS.separators("target/tests\\tests-Foo"));
        }
    }

    @Test
    public void testOSName()
    {
        String osname = System.getProperty("os.name");
        System.out.printf("os.name = \"%s\"%n",osname);

        // Tests if running under OSX
        if (osname.contains("Mac OS X"))
        {
            Assert.assertTrue("Should have detected as OSX",OS.IS_OSX);
            Assert.assertTrue("Should have also detected as Unix",OS.IS_UNIX);
            // False Tests
            Assert.assertFalse("Should not be windows",OS.IS_WINDOWS);
            Assert.assertFalse("Should not as Linux",OS.IS_LINUX);
            return;
        }

        // Tests if running under Linux
        if (osname.toLowerCase().contains("linux"))
        {
            Assert.assertTrue("Should have detected as Linux",OS.IS_LINUX);
            Assert.assertTrue("Should have also detected as Unix",OS.IS_UNIX);
            // False Tests
            Assert.assertFalse("Should not be windows",OS.IS_WINDOWS);
            Assert.assertFalse("Should not as OSX",OS.IS_OSX);
            return;
        }

        // Tests if running under Windows
        if (osname.toLowerCase().contains("windows"))
        {
            Assert.assertTrue("Should have detected as Windows",OS.IS_WINDOWS);
            // False Tests
            Assert.assertFalse("Should not be Unix",OS.IS_UNIX);
            Assert.assertFalse("Should not be Linux",OS.IS_LINUX);
            Assert.assertFalse("Should not as OSX",OS.IS_OSX);
            return;
        }
    }
}
