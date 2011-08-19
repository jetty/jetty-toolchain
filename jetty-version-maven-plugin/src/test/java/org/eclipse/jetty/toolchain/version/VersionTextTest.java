package org.eclipse.jetty.toolchain.version;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.PathAssert;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;


public class VersionTextTest
{
    @Rule
    public final TestingDir testdir = new TestingDir();

    @Test
    public void testReadVersionText() throws IOException {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("VERSION.txt");
        VersionText vt = new VersionText();
        vt.read(sampleVerText);

        Assert.assertEquals("Number of Releases", 383, vt.getReleases().size());

        Release r31rc9 = vt.findRelease("jetty-3.1.rc9");
        Assert.assertNotNull("Should have found release", r31rc9);
        Assert.assertEquals("[3.1.rc9].issues.size", 10, r31rc9.getIssues().size());
        
        Release r20a2 = vt.findRelease("jetty-2.0Alpha2");
        Assert.assertNotNull("Should have found release", r20a2);
        Assert.assertEquals("[2.0Alpha1].issues.size", 9, r20a2.getIssues().size());
    }
    
    @Test
    public void testReadVersion20Alpha2Text() throws IOException {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("version-2.0Alpha2.txt");
        VersionText vt = new VersionText();
        vt.read(sampleVerText);

        Assert.assertEquals("Number of Releases", 1, vt.getReleases().size());

        Release r20a2 = vt.findRelease("jetty-2.0Alpha2");
        Assert.assertNotNull("Should have found release", r20a2);
        Assert.assertEquals("[2.0Alpha1].issues.size", 9, r20a2.getIssues().size());
    }

    /**
     * Test bug that crops up with when the combination of parse/write(with wrapping)
     * results in a line that starts with "-D" that is mistakenly interpreted as the
     * start of another issue by the parsing step.
     */
    @Test
    public void testReadWriteVersion715Text() throws IOException {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("version-7.1.5.txt");
        testdir.ensureEmpty();

        // Read first time
        VersionText vt = new VersionText();
        vt.read(sampleVerText);
        
        // Write it out
        File out1 = testdir.getFile("version-7.1.5-a.txt");
        vt.write(out1);
        
        // Read generated
        vt = new VersionText();
        vt.read(out1);
        
        // Write it out again
        File out2 = testdir.getFile("version-7.1.5-b.txt");
        vt.write(out2);
        
        // Read it in one last time
        vt = new VersionText();
        vt.read(out2);

        Assert.assertEquals("Number of Releases", 1, vt.getReleases().size());

        Release rel = vt.findRelease("jetty-7.1.5.v20100705");
        Assert.assertNotNull("Should have found release", rel);
        Assert.assertEquals("[7.1.5].issues.size", 21, rel.getIssues().size());
    }

    @Test
    public void testWriteVersionText() throws IOException {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("VERSION.txt");
        VersionText vt = new VersionText();
        vt.read(sampleVerText);

        testdir.ensureEmpty();
        File outver = testdir.getFile("version-out.txt");
        vt.write(outver);

        PathAssert.assertFileExists("Output version.txt",outver);
    }
    
    @Test
    public void testGetPriorVersion_Top() throws IOException {
        assertPriorVersion("jetty-7.5.0-SNAPSHOT", "jetty-7.4.4.v20110707");
    }

    @Test
    public void testGetPriorVersion_Middle() throws IOException {
        assertPriorVersion("jetty-7.4.0.RC0", "jetty-7.3.1.v20110307");
    }

    private void assertPriorVersion(String startVersion, String expectedPriorVersion) throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("VERSION.txt");
        VersionText vt = new VersionText();
        vt.read(sampleVerText);
        
        String prior = vt.getPriorVersion(startVersion);
        Assert.assertEquals("Prior version", expectedPriorVersion, prior);
    }
}
