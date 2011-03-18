// ========================================================================
// Copyright (c) 2004-2009 Mort Bay Consulting Pty. Ltd.
// ------------------------------------------------------------------------
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Eclipse Public License v1.0
// and Apache License v2.0 which accompanies this distribution.
// The Eclipse Public License is available at 
// http://www.eclipse.org/legal/epl-v10.html
// The Apache License v2.0 is available at
// http://www.opensource.org/licenses/apache2.0.php
// You may elect to redistribute this code under either of these licenses. 
// ========================================================================

package org.eclipse.jetty.toolchain.test;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * IO Utilities.
 */
public class IO
{
    public static final int BUFFER_SIZE = 64 * 1024;

    /**
     * Copy Reader to Writer out until EOF or exception.
     */
    public static void copy(Reader in, Writer out) throws IOException
    {
        char buffer[] = new char[BUFFER_SIZE];
        int len = BUFFER_SIZE;

        while (true)
        {
            len = in.read(buffer,0,BUFFER_SIZE);
            if (len == -1)
                break;
            out.write(buffer,0,len);
        }
    }

    /**
     * closes an {@link Closeable}, and silently ignores exceptions
     * 
     * @param c
     *            the closeable to close
     */
    public static void close(Closeable c)
    {
        if (c == null)
        {
            return;
        }

        try
        {
            c.close();
        }
        catch (IOException ignore)
        {
            /* ignore */
        }
    }
}
