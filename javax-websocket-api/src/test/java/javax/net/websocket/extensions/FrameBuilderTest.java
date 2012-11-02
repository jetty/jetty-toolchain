//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
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

package javax.net.websocket.extensions;

import static org.hamcrest.Matchers.*;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

public class FrameBuilderTest {
    @Test
    public void testCreateBinary() {
	byte buf[] = new byte[] { 0x12, 0x34, 0x56 };
	ByteBuffer bb = ByteBuffer.wrap(buf);
	Frame.Data.Binary frame = FrameBuilder.getBuilder().createBinaryFrame(
		bb);
	Assert.assertNotNull("Frame", frame);
    }

    @Test
    public void testCreateBinaryContinuation() {
	byte buf[] = new byte[] { 0x12, 0x34, 0x56 };
	ByteBuffer bb = ByteBuffer.wrap(buf);
	Frame.Data.Binary.Continuation frame = FrameBuilder.getBuilder()
		.createBinaryContinuationFrame(bb, false);
	Assert.assertNotNull("Frame", frame);
	Assert.assertThat("Frame.isLast", frame.isLast(), is(false));
    }

    @Test
    public void testCreateText() {
	Frame.Data.Text frame = FrameBuilder.getBuilder().createTextFrame(
		"Hello world");
	Assert.assertNotNull("Frame", frame);
    }

    @Test
    public void testCreateTextContinuation() {
	Frame.Data.Text.Continuation frame = FrameBuilder.getBuilder()
		.createTextContinuationFrame("Hello world", false);
	Assert.assertNotNull("Frame", frame);
	Assert.assertThat("Frame.isLast", frame.isLast(), is(false));
    }

    @Test
    public void testGetBuilder() {
	// TODO
    }
}
