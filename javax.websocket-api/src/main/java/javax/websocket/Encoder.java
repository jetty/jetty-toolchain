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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.ByteBuffer;

/**
 * The Encoder interfaces defines how developers can provide a way to convert
 * their custom objects into web socket messages. The Encoder interface contains
 * subinterfaces that allow encoding algorithms to encode custom objects to:
 * text, binary data, character stream and write to an output stream.
 * 
 * @since DRAFT 002
 */
public interface Encoder {
    /**
     * This interface defines how to provide a way to convert a custom object
     * into a binary message.
     * 
     * @param <T>
     */
    interface Binary<T> extends Encoder {
	/**
	 * Encode the given object into a byte array.
	 * 
	 * @param object
	 *            the object being encoded
	 * @return the binary data
	 */
	ByteBuffer encode(T object) throws EncodeException;
    }

    /**
     * This interface may be implemented by encoding algorithms that want to
     * write the encoded object to a binary stream.
     * 
     * @since DRAFT 006 / EDR
     * @param <T>
     *            the type of the object this encoder can encode.
     */
    interface BinaryStream<T> extends Encoder {
	/**
	 * Encode the given object into a binary stream written to the
	 * implementation provided OutputStream.
	 * 
	 * @param object
	 *            the object being encoded
	 * @param os
	 *            the output stream where the encoded data is written
	 */
	void encode(T object, OutputStream os) throws EncodeException,
		IOException;
    }

    /**
     * This interface defines how to provide a way to convert a custom object
     * into a text message.
     * 
     * @param <T>
     */
    interface Text<T> extends Encoder {
	/**
	 * Encode the given object into a String.
	 * 
	 * @param object
	 *            the object being encoded
	 * @return the encoded object as a string
	 */
	String encode(T object) throws EncodeException;
    }

    /**
     * This interface may be implemented by encoding algorithms that want to
     * write the encoded object to a character stream.
     * 
     * @since DRAFT 006 / EDR
     * @param <T>
     *            the type of the object this encoder can encode.
     */
    interface TextStream<T> extends Encoder {
	/**
	 * Encode the given object to a character stream writing it to the
	 * supplied Writer. Implementations of this method may use the
	 * EncodeException to indicate a failure to convert the supplied object
	 * to an encoded form, and may use the IOException to indicate a failure
	 * to write the data to the supplied stream.
	 * 
	 * @param object
	 *            the object to be encoded
	 * @param writer
	 *            the writer provided by the web socket runtime to write the
	 *            encoded data
	 */
	void encode(T object, Writer writer) throws EncodeException,
		IOException;
    }
}
