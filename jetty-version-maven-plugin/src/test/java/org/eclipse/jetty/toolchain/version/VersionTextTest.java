/*******************************************************************************
 * Copyright (c) 2011 Intalio, Inc.
 * ======================================================================
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *   The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *
 *   The Apache License v2.0 is available at
 *   http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/
package org.eclipse.jetty.toolchain.version;

import static org.hamcrest.Matchers.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.toolchain.test.IO;
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

    private void assertPriorVersion(String startVersion, String expectedPriorVersion) throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("VERSION.txt");
        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(sampleVerText);

        String prior = vt.getPriorVersion(startVersion);
        Assert.assertEquals("Prior version",expectedPriorVersion,prior);
    }

    private void assertVersionList(List<String> expectedVersions, List<String> actualVersions)
    {
        boolean sizeMismatch = (expectedVersions.size() != actualVersions.size());

        int mismatchIdx = -1;
        int len = Math.min(expectedVersions.size(),actualVersions.size());
        for (int i = 0; i < len; i++)
        {
            if (!expectedVersions.get(i).equals(actualVersions.get(i)))
            {
                mismatchIdx = i;
                break;
            }
        }

        if ((mismatchIdx >= 0) || (sizeMismatch))
        {
            if (sizeMismatch && (mismatchIdx < 0))
            {
                mismatchIdx = len;
            }
            System.out.printf("Mismatch Index: %d%n",mismatchIdx);
            dumpStringListSection("Expected Versions",expectedVersions,mismatchIdx);
            dumpStringListSection("Actual Versions",actualVersions,mismatchIdx);

            StringBuilder err = new StringBuilder();
            err.append("Mismatch in lists encountered. [at index ").append(mismatchIdx);
            err.append("]");
            if (sizeMismatch)
            {
                err.append(" (expected.length=").append(expectedVersions.size());
                err.append(", actual.length=").append(actualVersions.size());
                err.append(")");
            }
            Assert.assertThat(err.toString(),mismatchIdx,lessThanOrEqualTo(0));
        }
    }

    private void dumpStringListSection(String header, List<String> strs, int offset)
    {
        if (strs == null)
        {
            System.out.printf("%s: <NULL>%n",header);
        }
        else
        {
            int start = Math.max(0,offset - 5);
            int end = Math.min(strs.size(),offset + 5);

            System.out.printf("%s: %d entries%n",header,strs.size());
            for (int i = start; i < end; i++)
            {
                System.out.printf("[%d] %s%n",i,strs.get(i));
            }
        }
    }

    private List<String> getExpectedVersions(String expectedTextPath) throws IOException
    {
        FileReader reader = null;
        BufferedReader buf = null;
        List<String> versions = new ArrayList<String>();
        try
        {
            File expectedFile = MavenTestingUtils.getTestResourceFile(expectedTextPath);
            reader = new FileReader(expectedFile);
            buf = new BufferedReader(reader);
            String line;
            while ((line = buf.readLine()) != null)
            {
                if (StringUtils.isBlank(line))
                {
                    // empty. skip
                    continue;
                }
                versions.add(line.trim());
            }
        }
        finally
        {
            IO.close(buf);
            IO.close(reader);
        }
        return versions;
    }

    @Test
    public void testGetPriorVersion_Middle() throws IOException
    {
        assertPriorVersion("jetty-7.4.0.RC0","jetty-7.3.1.v20110307");
    }

    @Test
    public void testGetPriorVersion_Top() throws IOException
    {
        assertPriorVersion("jetty-7.5.0-SNAPSHOT","jetty-7.4.4.v20110707");
    }

    @Test
    public void testReadCodehausVersionText() throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("version-codehaus.txt");
        VersionText vt = new VersionText(VersionPattern.CODEHAUS);
        vt.read(sampleVerText);

        Assert.assertEquals("Number of Releases",30,vt.getReleases().size());

        Release r740 = vt.findRelease("jetty@codehaus-7.4.0.v20110414");
        Assert.assertNotNull("Should have found release",r740);
        Assert.assertEquals("[7.4.0.v20110414].issues.size",2,r740.getIssues().size());

        Release r720rc0 = vt.findRelease("jetty@codehaus-7.2.0.RC0");
        Assert.assertNotNull("Should have found release",r720rc0);
        Assert.assertEquals("[7.2.0.RC0].issues.size",9,r720rc0.getIssues().size());
    }

    @Test
    public void testReadEclipseVersionText() throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("VERSION.txt");
        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(sampleVerText);

        List<String> actualVersions = vt.getVersionList();
        List<String> expectedVersions = getExpectedVersions("expected-versions-eclipse.txt");

        assertVersionList(expectedVersions,actualVersions);

        Release r31rc9 = vt.findRelease("jetty-3.1.rc9");
        Assert.assertNotNull("Should have found release",r31rc9);
        Assert.assertEquals("[3.1.rc9].issues.size",10,r31rc9.getIssues().size());

        Release r20a2 = vt.findRelease("jetty-2.0Alpha2");
        Assert.assertNotNull("Should have found release",r20a2);
        Assert.assertEquals("[2.0Alpha1].issues.size",9,r20a2.getIssues().size());
    }

    @Test
    public void testReadVersion20Alpha2Text() throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("version-2.0Alpha2.txt");
        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(sampleVerText);

        Assert.assertEquals("Number of Releases",1,vt.getReleases().size());

        Release r20a2 = vt.findRelease("jetty-2.0Alpha2");
        Assert.assertNotNull("Should have found release",r20a2);
        Assert.assertEquals("[2.0Alpha1].issues.size",9,r20a2.getIssues().size());
    }

    /**
     * Test bug that crops up with when the combination of parse/write(with wrapping) results in a line that starts with
     * "-D" that is mistakenly interpreted as the start of another issue by the parsing step.
     */
    @Test
    public void testReadWriteVersion715Text() throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("version-7.1.5.txt");
        testdir.ensureEmpty();

        // Read first time
        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(sampleVerText);

        // Write it out
        File out1 = testdir.getFile("version-7.1.5-a.txt");
        vt.write(out1);

        // Read generated
        vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(out1);

        // Write it out again
        File out2 = testdir.getFile("version-7.1.5-b.txt");
        vt.write(out2);

        // Read it in one last time
        vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(out2);

        Assert.assertEquals("Number of Releases",1,vt.getReleases().size());

        Release rel = vt.findRelease("jetty-7.1.5.v20100705");
        Assert.assertNotNull("Should have found release",rel);
        Assert.assertEquals("[7.1.5].issues.size",21,rel.getIssues().size());
    }

    @Test
    public void testWriteCodehausVersionText() throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("version-codehaus.txt");
        VersionText vt = new VersionText(VersionPattern.CODEHAUS);
        vt.read(sampleVerText);

        testdir.ensureEmpty();
        File outver = testdir.getFile("version-out.txt");
        vt.write(outver);

        PathAssert.assertFileExists("Output version.txt",outver);
    }

    @Test
    public void testWriteEclipseVersionText() throws IOException
    {
        File sampleVerText = MavenTestingUtils.getTestResourceFile("VERSION.txt");
        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.read(sampleVerText);

        testdir.ensureEmpty();
        File outver = testdir.getFile("version-out.txt");
        vt.write(outver);

        PathAssert.assertFileExists("Output version.txt",outver);
    }
}
