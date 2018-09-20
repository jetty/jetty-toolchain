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

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("javadoc")
public class StringManglerTest
{
    @Test
    public void testCondensePackageString()
    {
        assertThat(StringMangler.condensePackageString("org.eclipse.jetty.test.FooTest"),is("oejt.FooTest"));
        assertThat(StringMangler.condensePackageString("org.eclipse.jetty.server.logging.LogTest"),is("oejsl.LogTest"));
    }

    @Test
    public void testMaxStringLength()
    {
        assertThat(StringMangler.maxStringLength(9,"Eatagramovabits"),is("Eat...its"));
        assertThat(StringMangler.maxStringLength(10,"Eatagramovabits"),is("Eat...bits"));
        assertThat(StringMangler.maxStringLength(11,"Eatagramovabits"),is("Eata...bits"));
    }
}
