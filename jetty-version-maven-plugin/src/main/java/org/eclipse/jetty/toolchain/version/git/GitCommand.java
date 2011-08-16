package org.eclipse.jetty.toolchain.version.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.codehaus.plexus.util.IOUtil;
import org.eclipse.jetty.toolchain.version.Release;
import org.eclipse.jetty.toolchain.version.issues.Issue;

public class GitCommand
{
    private static class OutputHandler extends Thread
    {
        private final InputStream in;
        private final Log log;
        private final GitOutputParser parser;

        public OutputHandler(Log log, InputStream in, GitOutputParser parser)
        {
            this.log = log;
            this.in = in;
            this.parser = parser;
        }

        @Override
        public void run()
        {
            int linenum = 0;
            parser.parseStart();
            InputStreamReader reader = null;
            BufferedReader buf = null;
            try
            {
                reader = new InputStreamReader(in);
                buf = new BufferedReader(reader);
                String line;
                while ((line = buf.readLine()) != null)
                {
                    linenum++;
                    parser.parseLine(linenum,line);
                }
            }
            catch (IOException e)
            {
                if (!e.getMessage().equalsIgnoreCase("Stream closed"))
                {
                    log.debug(e);
                }
            }
            finally
            {
                IOUtil.close(buf);
                IOUtil.close(reader);
            }
            parser.parseEnd();
            log.debug("Parsed " + linenum + " lines of output");
        }
    }

    private Log log;
    private File workDir;

    private void execGitCommand(GitOutputParser outputParser, String... commands) throws IOException
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
            getLog().debug("Exit code: " + process.waitFor());
            handler.join();
        }
        catch (InterruptedException e)
        {
            getLog().error("Process didn't complete",e);
        }
        finally
        {
            IOUtil.close(in);
        }
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
