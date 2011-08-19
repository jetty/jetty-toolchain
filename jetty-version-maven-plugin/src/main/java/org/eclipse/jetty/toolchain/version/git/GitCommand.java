package org.eclipse.jetty.toolchain.version.git;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.util.IOUtil;
import org.eclipse.jetty.toolchain.version.Release;
import org.eclipse.jetty.toolchain.version.issues.Issue;

public class GitCommand
{
    private Log log;
    private File workDir;

    private int execGitCommand(GitOutputParser outputParser, String... commands) throws IOException
    {
        if (getLog().isDebugEnabled())
        {
            StringBuilder dbg = new StringBuilder();
            for (String cmd : commands)
            {
                dbg.append(" ").append(cmd);
            }
            getLog().debug("Command Line:" + dbg.toString());
        }

        ProcessBuilder pb = new ProcessBuilder(commands);
        pb.directory(getWorkDir());
        Process process = pb.start();

        InputStream in = null;
        try
        {
            in = process.getInputStream();
            OutputHandler handler = new OutputHandler(getLog(),in,outputParser);
            handler.start();
            int exitCode = process.waitFor();
            getLog().debug("Exit code: " + exitCode);
            handler.join();
            return exitCode;
        }
        catch (InterruptedException e)
        {
            getLog().error("Process didn't complete",e);
            throw new IOException("Process did not complete",e);
        }
        finally
        {
            IOUtil.close(in);
        }
    }

    public boolean fetchTags() throws IOException
    {
        Git2LogParser logout = new Git2LogParser(this.log,"fetch tags");
        int exitCode = execGitCommand(logout,"git","fetch","--tags");
        return (exitCode == 0);
    }

    public String findTagMatching(String version) throws IOException
    {
        for (String tag : getTags())
        {
            if (tag.startsWith(version))
            {
                return "tags/" + tag;
            }
        }
        return null;
    }

    public List<GitCommit> getCommitLog(String fromCommitId) throws IOException
    {
        GitLogParser logs = new GitLogParser();
        execGitCommand(logs,"git","log",fromCommitId + "..HEAD",logs.getFormat());
        return logs.getGitCommitLogs();
    }

    public Log getLog()
    {
        if (log == null)
        {
            log = new SystemStreamLog();
        }
        return log;
    }

    public String getTagCommitId(String tagId) throws IOException
    {
        GitLogParser logs = new GitLogParser();
        execGitCommand(logs,"git","log","-1",tagId,logs.getFormat());
        getLog().debug("Captured " + logs.getGitCommitLogs().size() + " log entries");
        GitCommit commit = logs.getGitCommitLog(0);
        return commit.getCommitId();
    }

    public List<String> getTags() throws IOException
    {
        GitTagParser tags = new GitTagParser();
        execGitCommand(tags,"git","tag","-l");
        return tags.getTagIds();
    }

    public File getWorkDir()
    {
        if (workDir == null)
        {
            workDir = new File(System.getProperty("user.dir"));
        }
        return workDir;
    }

    public void populateIssuesForRange(String fromCommitId, String toCommitId, Release rel) throws IOException
    {
        GitLogParser parser = new GitLogParser();
        execGitCommand(parser,"git","log",fromCommitId + ".." + toCommitId,parser.getFormat());
        getLog().debug("Captured " + parser.getGitCommitLogs().size() + " log entries");

        List<Issue> issues = parser.getIssues();
        getLog().info("Found " + issues.size() + " issues");
        rel.setExisting(false);
        rel.addIssues(issues);
    }

    public void setLog(Log log)
    {
        this.log = log;
    }

    public void setWorkDir(File basedir)
    {
        this.workDir = basedir;
    }
}
