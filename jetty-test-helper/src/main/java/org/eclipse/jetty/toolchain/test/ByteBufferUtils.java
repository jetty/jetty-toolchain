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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

class ByteBufferUtils
{
    /**
     * Get the byte array out of a ByteBuffer.
     *
     * @param buffer the buffer to get the array from
     * @return the byte buffer array
     */

    public static byte[] toArray(ByteBuffer buffer)
    {
        if (buffer.hasArray())
        {
            byte[] array = buffer.array();
            int from = buffer.arrayOffset() + buffer.position();
            return Arrays.copyOfRange(array, from, from + buffer.remaining());
        }
        else
        {
            byte[] to = new byte[buffer.remaining()];
            buffer.slice().get(to);
            return to;
        }
    }

    /**
     * Convert the ByteBuffer to a UTF-8 String.
     *
     * @param buffer the buffer to convert
     * @return the String form of the buffer
     */
    public static String toString(ByteBuffer buffer)
    {
        if (buffer == null)
            return null;
        byte[] array = buffer.hasArray() ? buffer.array() : null;
        if (array == null)
        {
            byte[] to = new byte[buffer.remaining()];
            buffer.slice().get(to);
            return new String(to, 0, to.length, StandardCharsets.UTF_8);
        }
        return new String(array, buffer.arrayOffset() + buffer.position(), buffer.remaining(), StandardCharsets.UTF_8);
    }
}
