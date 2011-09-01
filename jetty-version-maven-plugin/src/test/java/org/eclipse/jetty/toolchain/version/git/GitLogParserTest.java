package org.eclipse.jetty.toolchain.version.git;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.TestingDir;
import org.eclipse.jetty.toolchain.version.Release;
import org.eclipse.jetty.toolchain.version.VersionPattern;
import org.eclipse.jetty.toolchain.version.VersionText;
import org.eclipse.jetty.toolchain.version.issues.Issue;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class GitLogParserTest extends AbstractGitTestCase
{
    @Rule
    public TestingDir testdir = new TestingDir();

    @Test
    public void testParseGitLogTag() throws IOException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-specific-tag.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",1,parser.getGitCommitLogs().size());
        GitCommit commit = parser.getGitCommitLog(0);
        Assert.assertNotNull("parser.getGitCommitLog(0)",commit);
        Assert.assertEquals("commit.id","5aa94f502e5efe68628cff0378f44ff00619c493",commit.getCommitId());
    }

    @Test
    public void testParseIssueIds() throws IOException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-to-commit.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        List<Issue> issues = parser.getIssues();
        Assert.assertEquals("Commit entries with Issue IDs",95,issues.size());

        Release rel = new Release("TEST-VERSION");
        rel.setExisting(false);
        rel.addIssues(issues);

        VersionText vt = new VersionText(VersionPattern.ECLIPSE);
        vt.addRelease(rel);

        testdir.ensureEmpty();
        File outfile = testdir.getFile("test-ver.txt");
        vt.write(outfile);

        // TODO: compare output
    }

    @Test
    public void testParseLongGitLog() throws IOException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-to-commit.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",255,parser.getGitCommitLogs().size());

        int jesseCount = 0;
        for (GitCommit commit : parser.getGitCommitLogs())
        {
            if (commit.getAuthorName().contains("Jesse"))
            {
                jesseCount++;
            }
        }

        Assert.assertEquals("Commits by Jesse",79,jesseCount);
    }

    @Test
    public void testParseSingleGitLog() throws IOException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-log-specific-commit.txt");
        GitLogParser parser = new GitLogParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.gitCommitLogs",parser.getGitCommitLogs());
        Assert.assertEquals("parser.gitCommitLogs.size",1,parser.getGitCommitLogs().size());
        GitCommit commit = parser.getGitCommitLog(0);
        Assert.assertNotNull("parser.getGitCommitLog(0)",commit);
        Assert.assertEquals("commit.id","596fa1bd4edebc21de0389ff70b10b8060667ed1",commit.getCommitId());
    }
}
