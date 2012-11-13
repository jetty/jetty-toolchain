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

package javax.websocket;

import java.nio.ByteBuffer;

/**
 * A general exception that occurs when trying to decode a custom object from a
 * text or binary message.
 * 
 * @since DRAFT 002
 */
public class DecodeException extends Exception {
    private ByteBuffer bytes;
    private String text;

    /**
     * Constructs a DecodedException with the given ByteBuffer that cannot be
     * decoded, and reason why. The buffer may represent the whole message, or
     * part of the message, depending whether the application is using one of
     * the streaming methods or not.
     * 
     * @param bb
     * @param message
     */
    public DecodeException(ByteBuffer bb, String message) {
	super(message);
	this.bytes = bb;
    }

    public DecodeException(ByteBuffer bb, String message, Throwable cause) {
	super(message, cause);
	this.bytes = bb;
    }

    /**
     * Constructs a DecodedException with the given encoded string that cannot
     * be decoded, and reason why. The encoded string may represent the whole
     * message, or part of the message, depending whether the application is
     * using one of the streaming methods or not.
     * 
     * @param encodedString
     * @param message
     */
    public DecodeException(String encodedString, String message) {
	super(message);
	this.text = encodedString;
    }

    public DecodeException(String encodedString, String message, Throwable cause) {
	super(message, cause);
	this.text = encodedString;
    }

    /**
     * Return the ByteBuffer that cannot be decoded.
     */
    public ByteBuffer getBytes() {
	return bytes;
    }

    /**
     * Return the encoded string that cannot be decoded.
     */
    public String getText() {
	return text;
    }
}
