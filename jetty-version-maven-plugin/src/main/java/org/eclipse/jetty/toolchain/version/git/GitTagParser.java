package org.eclipse.jetty.toolchain.version.git;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;

public class GitTagParser implements GitOutputParser
{
    private final List<String> tagIds = new ArrayList<String>();

    public List<String> getTagIds()
    {
        return tagIds;
    }

    @Override
    public void parseEnd()
    {
        /* ignore */
    }

    @Override
    public void parseLine(int linenum, String line) throws IOException
    {
        if (StringUtils.isNotBlank(line))
        {
            tagIds.add(line.trim());
        }
    }

    @Override
    public void parseStart()
    {
        /* ignore */
    }
}
