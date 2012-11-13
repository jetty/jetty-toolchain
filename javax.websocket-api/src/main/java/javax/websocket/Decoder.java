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
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

/**
 * The Decoder interface holds member interfaces that define how a developer can
 * provide the web socket container a way web socket messages into developer
 * defined custom objects.
 * 
 * @since DRAFT 002
 */
public interface Decoder {
    /**
     * This interface defines how a custom object (of type T) is decoded from a
     * web socket message in the form of a byte buffer.
     */
    interface Binary<T> extends Decoder {
	/**
	 * Decode the given bytes into an object of type T.
	 * 
	 * @param bytes
	 *            the bytes to be decoded.
	 * @return the decoded object
	 */
	T decode(ByteBuffer bytes) throws DecodeException;

	/**
	 * Answer whether the given bytes can be decoded into an object of type
	 * T.
	 * 
	 * @param bytes
	 * @return whether or not the bytes can be decoded by this decoder.
	 */
	boolean willDecode(ByteBuffer bytes);
    }

    /**
     * This interface defines how a custom object is decoded from a web socket
     * message in the form of a binary stream.
     */
    interface BinaryStream<T> extends Decoder {
	/**
	 * Decode the given bytes read from the input stream into an object of
	 * type T.
	 * 
	 * @param is
	 *            the input stream carrying the bytes
	 */
	T decode(InputStream is) throws DecodeException, IOException;
    }

    /**
     * This interface defines how a custom object is decoded from a web socket
     * message in the form of a string.
     */
    interface Text<T> extends Decoder {
	/**
	 * Decode the given String into an object of type T.
	 * 
	 * @param s
	 *            the string to be decoded
	 * @return the decoded message as an object of type T
	 */
	T decode(String s) throws DecodeException;

	/**
	 * Answer whether the given String can be decoded into an object of type
	 * T.
	 * 
	 * @param s
	 *            the string being tested for decodability
	 * @return whether this decoder can decoded the supplied string.
	 */
	boolean willDecode(String s);
    }

    /**
     * This interface defines how a custom object of type T is decoded from a
     * web socket message in the form of a character stream.
     */
    interface TextStream<T> extends Decoder {
	/**
	 * Reads the websocket message from the implementation provided Reader
	 * and decodes it into an instance of the supplied object type.
	 * 
	 * @param reader
	 *            the reader from which to read the web socket message.
	 * @return the instance of the object that is the decoded web socket
	 *         message.
	 */
	T decode(Reader reader) throws DecodeException, IOException;
    }
}
