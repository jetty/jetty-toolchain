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

package javax.websocket.extensions;

import java.nio.ByteBuffer;

/**
 * A factory class for building Frames.
 * 
 * @since DRAFT 003
 */
public class FrameBuilder {
    private static class BinaryFrame implements Frame.Data.Binary.Continuation {
	private byte[] buf;
	private boolean isLast;

	public BinaryFrame(ByteBuffer message) {
	    this(message, false);
	}

	public BinaryFrame(ByteBuffer bb, boolean isLast) {
	    int len = bb.remaining();
	    this.buf = new byte[len];
	    if (len > 0) {
		bb.get(buf, 0, len);
	    }
	    this.isLast = isLast;
	}

	@Override
	public byte[] getData() {
	    return this.buf;
	}

	@Override
	public byte[] getExtensionData() {
	    return null; // TODO
	}

	@Override
	public boolean isLast() {
	    return this.isLast;
	}
    }

    private static class TextFrame implements Frame.Data.Text.Continuation {
	private String text;
	private boolean isLast;

	public TextFrame(String message) {
	    this(message, true);
	}

	public TextFrame(String text, boolean isLast) {
	    this.text = text;
	    this.isLast = isLast;
	}

	@Override
	public byte[] getExtensionData() {
	    return null; // TODO
	}

	@Override
	public String getText() {
	    return text;
	}

	@Override
	public boolean isLast() {
	    return isLast;
	}
    }

    private static FrameBuilder INSTANCE = new FrameBuilder();

    public static FrameBuilder getBuilder() {
	return INSTANCE;
    }

    // FIXME: mark as private?
    public FrameBuilder() {
	/* default constructor */
    }

    /**
     * Create a partial binary frame with the given string fragment, and
     * indication of whether this is the last or not of a series.
     * 
     * @param bb
     * @param isLast
     */
    public Frame.Data.Binary.Continuation createBinaryContinuationFrame(
	    ByteBuffer bb, boolean isLast) {
	return new BinaryFrame(bb, isLast);
    }

    /**
     * Create a binary data frame with the given bytes.
     */
    public Frame.Data.Binary createBinaryFrame(final ByteBuffer bb) {
	return new BinaryFrame(bb);
    }

    /**
     * Create a partial text frame with the given string fragment, and
     * indication of whether this is the last or not of a series.
     * 
     * @param s
     * @param isLast
     */
    public Frame.Data.Text.Continuation createTextContinuationFrame(String s,
	    boolean isLast) {
	return new TextFrame(s, isLast);
    }

    /**
     * Create a text frame with the given string data.
     */
    public Frame.Data.Text createTextFrame(final String s) {
	return new TextFrame(s);
    }
}
