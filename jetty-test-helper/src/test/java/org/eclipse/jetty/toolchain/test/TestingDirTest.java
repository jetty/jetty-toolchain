package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class TestingDirTest
{
    @Rule
    public TestingDir testingdir = new TestingDir();

    @Test
    public void testGetPath() throws IOException
    {
        String expected = OS.separators("/target/tests/");
        Path dir = testingdir.getPath();
        String fullpath = dir.toString();

        Assert.assertThat(fullpath,containsString(expected));
    }
}
