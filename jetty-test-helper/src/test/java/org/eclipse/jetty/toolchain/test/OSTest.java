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

import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.junit.jupiter.api.condition.OS.WINDOWS;

import org.junit.Assert;
import org.junit.jupiter.api.condition.EnabledOnOs;

@SuppressWarnings("javadoc")
public class OSTest
{
    @EnabledOnOs(WINDOWS)
    public void testSeparatorsWindows()
    {
        Assert.assertEquals("target\\tests\\tests-Foo",OS.separators("target/tests/tests-Foo"));
        Assert.assertEquals("target\\tests\\tests-Foo",OS.separators("target/tests\\tests-Foo"));
    }

    @EnabledOnOs({LINUX, MAC})
    public void testSeparatorsUnix()
    {
        Assert.assertEquals("target/tests/tests-Foo",OS.separators("target/tests/tests-Foo"));
        Assert.assertEquals("target/tests/tests-Foo",OS.separators("target/tests\\tests-Foo"));
    }
}
