package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.CoreMatchers.*;
import java.io.File;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class TestingDirTest
{
    @Rule
    public TestingDir testingdir = new TestingDir();

    @Test
    public void testGetDir()
    {
        String expected = OS.separators("/target/tests/");
        File dir = testingdir.getDir();
        String fullpath = dir.getAbsolutePath();

        Assert.assertThat(fullpath,containsString(expected));
    }
}
