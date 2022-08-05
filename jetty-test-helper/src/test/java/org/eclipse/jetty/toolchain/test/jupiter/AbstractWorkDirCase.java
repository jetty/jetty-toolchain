//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
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
