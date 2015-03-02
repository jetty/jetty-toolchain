//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class JDKTest
{
    @Test
    public void testJdk()
    {
        // Running version of Java
        String ver = System.getProperty("java.version");
        if (ver.startsWith("1.5."))
        {
            System.err.println("Testing JDK.IS_5 - " + ver);
            assertThat("JVM(" + ver + ") / JDK.IS_5",JDK.IS_5,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_6",JDK.IS_6,is(false));
            assertThat("JVM(" + ver + ") / JDK.IS_7",JDK.IS_7,is(false));
            assertThat("JVM(" + ver + ") / JDK.IS_8",JDK.IS_8,is(false));
            assertThat("JVM(" + ver + ") / JDK.IS_9",JDK.IS_9,is(false));
        }
        else if (ver.startsWith("1.6."))
        {
            System.err.println("Testing JDK.IS_6 - " + ver);
            assertThat("JVM(" + ver + ") / JDK.IS_5",JDK.IS_5,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_6",JDK.IS_6,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_7",JDK.IS_7,is(false));
            assertThat("JVM(" + ver + ") / JDK.IS_8",JDK.IS_8,is(false));
            assertThat("JVM(" + ver + ") / JDK.IS_9",JDK.IS_9,is(false));
        }
        else if (ver.startsWith("1.7."))
        {
            System.err.println("Testing JDK.IS_7 - " + ver);
            assertThat("JVM(" + ver + ") / JDK.IS_5",JDK.IS_5,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_6",JDK.IS_6,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_7",JDK.IS_7,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_8",JDK.IS_8,is(false));
            assertThat("JVM(" + ver + ") / JDK.IS_9",JDK.IS_9,is(false));
        }
        else if (ver.startsWith("1.8."))
        {
            System.err.println("Testing JDK.IS_8 - " + ver);
            assertThat("JVM(" + ver + ") / JDK.IS_5",JDK.IS_5,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_6",JDK.IS_6,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_7",JDK.IS_7,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_8",JDK.IS_8,is(true));
            assertThat("JVM(" + ver + ") / JDK.IS_9",JDK.IS_9,is(false));
        }
    }
}
