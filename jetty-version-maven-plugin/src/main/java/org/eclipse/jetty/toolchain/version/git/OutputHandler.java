package org.eclipse.jetty.toolchain.version.git;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.IOUtil;

public class OutputHandler extends Thread
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