//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.CoreMatchers.containsString;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class TestingDirTest
{
    @Rule
    public TestingDir testingdir = new TestingDir();

    @Test
    public void testGetPath() throws IOException
    {
        String expected = OS.separators("/target/tests/");
        Path dir = testingdir.getPath();
        String fullpath = dir.toString();

        Assert.assertThat(fullpath, containsString(expected));
    }
}
