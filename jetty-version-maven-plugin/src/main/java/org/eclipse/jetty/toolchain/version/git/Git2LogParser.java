package org.eclipse.jetty.toolchain.version.git;

import java.io.IOException;

import org.apache.maven.plugin.logging.Log;

public class Git2LogParser implements GitOutputParser
{
    private Log log;
    private String commandId;

    public Git2LogParser(Log log, String commandId)
    {
        this.log = log;
        this.commandId = commandId;
    }

    @Override
    public void parseEnd()
    {
        /* ignore */
    }

    @Override
    public void parseLine(int linenum, String line) throws IOException
    {
        log.debug("[" + commandId + "]: " + line);
    }

    @Override
    public void parseStart()
    {
        /* ignore */
    }
}
