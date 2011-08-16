package org.eclipse.jetty.toolchain.version.git;

import java.io.IOException;

public interface GitOutputParser
{
    void parseEnd();

    void parseLine(int linenum, String line) throws IOException;

    void parseStart();
}
