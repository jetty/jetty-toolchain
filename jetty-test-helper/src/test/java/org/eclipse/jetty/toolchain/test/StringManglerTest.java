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
