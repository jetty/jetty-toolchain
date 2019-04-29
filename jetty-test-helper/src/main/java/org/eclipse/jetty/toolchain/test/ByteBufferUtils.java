//
//  ========================================================================
//  Copyright (c) 1995-2019 Mort Bay Consulting Pty. Ltd.
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

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ByteBufferUtils
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
