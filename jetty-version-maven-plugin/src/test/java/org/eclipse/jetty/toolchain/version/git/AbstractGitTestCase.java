package org.eclipse.jetty.toolchain.version.git;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.codehaus.plexus.util.IOUtil;

public abstract class AbstractGitTestCase
{
    protected void parseSampleFile(GitOutputParser parser, File sampleFile) throws IOException
    {
        FileReader reader = null;
        BufferedReader buf = null;
        try
        {
            reader = new FileReader(sampleFile);
            buf = new BufferedReader(reader);
            int linenum = 0;
            String line;
            parser.parseStart();
            while ((line = buf.readLine()) != null)
            {
                linenum++;
                parser.parseLine(linenum,line);
            }
            parser.parseEnd();
        }
        finally
        {
            IOUtil.close(buf);
            IOUtil.close(reader);
        }
    }
}
