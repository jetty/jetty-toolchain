//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
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

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Test;

public class StringManglerTest
{
    @Test
    public void testCondensePackageString()
    {
        Assert.assertThat(StringMangler.condensePackageString("org.eclipse.jetty.test.FooTest"),is("oejt.FooTest"));
        Assert.assertThat(StringMangler.condensePackageString("org.eclipse.jetty.server.logging.LogTest"),is("oejsl.LogTest"));
    }

    @Test
    public void testMaxStringLength()
    {
        Assert.assertThat(StringMangler.maxStringLength(9,"Eatagramovabits"),is("Eat...its"));
        Assert.assertThat(StringMangler.maxStringLength(10,"Eatagramovabits"),is("Eat...bits"));
        Assert.assertThat(StringMangler.maxStringLength(11,"Eatagramovabits"),is("Eata...bits"));
    }
}
