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

import java.nio.ByteBuffer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class ByteBufferAssert
{
    public static void assertEquals(String message, byte[] expected, byte[] actual)
    {
        assertThat(message + " byte[].length", actual.length, is(expected.length));
        int len = expected.length;
        for (int i = 0; i < len; i++)
        {
            assertThat(message + " byte[" + i + "]", actual[i], is(expected[i]));
        }
    }

    public static void assertEquals(String message, ByteBuffer expectedBuffer, ByteBuffer actualBuffer)
    {
        if (expectedBuffer == null)
        {
            assertThat(message, actualBuffer, nullValue());
        }
        else
        {
            byte expectedBytes[] = ByteBufferUtils.toArray(expectedBuffer);
            byte actualBytes[] = ByteBufferUtils.toArray(actualBuffer);
            assertEquals(message, expectedBytes, actualBytes);
        }
    }

    public static void assertEquals(String message, String expectedString, ByteBuffer actualBuffer)
    {
        String actualString = ByteBufferUtils.toString(actualBuffer);
        assertThat(message, actualString, is(expectedString));
    }

    public static void assertSize(String message, int expectedSize, ByteBuffer buffer)
    {
        if ((expectedSize == 0) && (buffer == null))
        {
            return;
        }
        assertThat(message + " buffer.remaining", buffer.remaining(), is(expectedSize));
    }
}
