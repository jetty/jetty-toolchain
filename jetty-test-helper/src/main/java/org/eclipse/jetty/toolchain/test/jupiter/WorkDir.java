//
//  ========================================================================
//  Copyright (c) 1995-2020 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.toolchain.test.jupiter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.FS;

public class WorkDir
{
    private final Path path;

    public WorkDir(Path path)
    {
        this.path = path;
    }

    /**
     * Get the test specific directory to use for work directory.
     * <p>
     * Name is derived from the test classname &amp; method name.
     *
     * @return the test specific directory.
     */
    public Path getPath()
    {
        if (Files.exists(path))
        {
            return path;
        }

        FS.ensureDirExists(path);
        try
        {
            return path.normalize().toRealPath();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Unable to obtain real path: " + path, e);
        }
    }

    /**
     * Get a {@link Path} file reference for content inside of the test specific work directory.
     * <p>
     * Note: No assertions are made if the file exists or not.
     *
     * @param name
     *            the path name of the file (supports deep paths)
     * @return the file reference.
     */
    public Path getPathFile(String name)
    {
        return path.resolve(name);
    }

    /**
     * Ensure that the work directory is empty.
     * <p>
     * Useful for repeated testing without using the maven <code>clean</code> goal (such as within Eclipse).
     */
    public void ensureEmpty()
    {
        FS.ensureEmpty(path);
    }

    /**
     * Get the unique work directory while ensuring that it is empty (if not).
     *
     * @return the unique work directory, created, and empty.
     */
    public Path getEmptyPathDir()
    {
        FS.ensureEmpty(path);
        return path;
    }
}
