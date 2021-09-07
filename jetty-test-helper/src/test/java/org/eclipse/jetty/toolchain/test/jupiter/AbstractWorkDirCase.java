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
import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.FS;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractWorkDirCase
{
    public WorkDir workDir;

    @BeforeEach
    public void setup() throws IOException
    {
        Path path = workDir.getEmptyPathDir();
        Path dir = path.resolve("dir");
        FS.ensureDirExists(dir);
        FS.touch(dir.resolve("hello.txt"));
    }
}
