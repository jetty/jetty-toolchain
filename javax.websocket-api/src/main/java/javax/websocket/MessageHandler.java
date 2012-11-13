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

import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;

/**
 * Developers implement MessageHandlers in order to receive incoming messages
 * during a web socket conversation. Each web socket session uses no more than
 * one thread at a time to call its MessageHandlers. This means that, provided
 * each message handler instance is used to handle messages for one web socket
 * session, at most one thread at a time can be calling any of its methods.
 * Developers who wish to handle messages from multiple clients within the same
 * message handlers may do so by adding the same instance as a handler on each
 * of the Session objects for the clients. In that case, they will need to code
 * with the possibility of their MessageHandler being called concurrently by
 * multiple threads, each one arising from a different client session.
 * 
 * @since DRAFT 001
 */
public interface MessageHandler {
    /**
     * This kind of handler is called to process for binary messages which may
     * arrive in multiple parts. A single binary message may consist of 0 to n
     * calls to this method where @param last is false followed by a single call
     * with @param last set to true. Messages do not interleave and the parts
     * arrive in order.
     */
    interface AsyncBinary extends MessageHandler {
	/**
	 * Called when part of a binary message has been received.
	 * 
	 * @param part
	 *            the fragment of the message received.
	 * @param last
	 *            whether or not this is last in the sequence of parts of
	 *            the message
	 */
	void onMessagePart(ByteBuffer part, boolean last);
    }

    /**
     * This kind of handler is called to process for text messages which may
     * arrive in multiple parts. A single text message may consist of 0 to n
     * calls to this method where @param last is false followed by a single call
     * with @param last set to true. Messages do not interleave and the parts
     * arrive in order.
     */
    interface AsyncText extends MessageHandler {
	/**
	 * Called when part of a text message has been received.
	 * 
	 * @param part
	 *            The fragment of the message received
	 * @param last
	 *            Whether or not this is last in the sequence of parts of
	 *            the message.
	 */
	void onMessagePart(String part, boolean last);
    }

    /**
     * This kind of listener listens for binary messages. If the message is
     * received in parts, the container buffers it until it is has been fully
     * received before this method is called.
     * 
     * @since DRAFT 002
     */
    interface Binary extends MessageHandler {
	/**
	 * Called when the binary message has been fully received.
	 * 
	 * @param data
	 *            the binary message data
	 */
	void onMessage(ByteBuffer data);
    }

    /**
     * This kind of handler is called when a new binary message arrives that is
     * to be read using a blocking stream. Since: DRAFT 002
     */
    interface BinaryStream extends MessageHandler {
	/**
	 * This method is called when a new binary message has begun to arrive.
	 * The InputStream passed in allows implementors of this handler to read
	 * the message in a blocking manner. The read methods on the InputStream
	 * block until message data is available. A new input stream is created
	 * for each incoming message.
	 * 
	 * @param is
	 *            the input stream containing the message
	 */
	void onMessage(InputStream is);
    }

    /**
     * This kind of handler is called when a new text message arrives that is to
     * be read using a blocking stream.
     * 
     * @since DRAFT 002
     */
    interface CharacterStream extends MessageHandler {
	/**
	 * This method is called when a new text message has begun to arrive.
	 * The Reader passed in allows implementors of this handler to read the
	 * message in a blocking manner. The read methods on the Reader block
	 * until message data is available. A new reader is created for each
	 * incoming message.
	 * 
	 * @param r
	 *            the reader containing the message
	 */
	void onMessage(Reader r);
    }

    /**
     * This kind of listener listens for messages that the container knows how
     * to decode into an object of type T. This will involve providing the
     * endpoint configuration a decoder for objects of type T.
     * 
     * @since DRAFT 002
     */
    interface DecodedObject<T> extends MessageHandler {
	/**
	 * Called when the container receives a message that it has been able to
	 * decode into an object of type T. Containers will by default be able
	 * to encode java primitive types, their object equivalents, and arrays
	 * or collections thereof.
	 * 
	 * @param customObject
	 *            the message being sent
	 */
	void onMessage(T customObject);
    }

    /**
     * This handler is called back by the container when the container receives
     * a pong message.
     */
    interface Pong extends MessageHandler {
	/**
	 * Called when the container receives a pong message containing the
	 * given application data.
	 */
	void onPong(ByteBuffer applicationData);
    }

    /**
     * This kind of listener listens for text messages. If the message is
     * received in parts, the container buffers it until it is has been fully
     * received before this method is called. Since: DRAFT 002
     */
    interface Text extends MessageHandler {
	/**
	 * Called when the text message has been fully received.
	 * 
	 * @param text
	 *            the binary message data FIXME
	 */
	void onMessage(java.lang.String text);
    }
}
