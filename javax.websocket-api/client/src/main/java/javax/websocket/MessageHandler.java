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
 * <br>
 * <br>
 * See {@link Endpoint} for a usage example.
 * 
 * @see DRAFT 013
 */
public interface MessageHandler {
    /**
     * This kind of handler is notified by the implementation as it becomes
     * ready to deliver parts of a whole message. <br/>
     * <br/>
     * For handling parts of text messages, the type T is
     * {@link java.lang.String} <br/>
     * <br/>
     * For handling parts of binary messages, the allowable types for T are
     * <ul>
     * <li>{@link java.nio.ByteBuffer}</li>
     * <li>byte[]</li>
     * </ul>
     * <br/>
     * <p/>
     * Developers should not continue to reference message objects of type
     * {@link java.nio.ByteBuffer} after the completion of the onMessage() call,
     * since they may be recycled by the implementation. <br/>
     * <br/>
     * Note: Implementations may choose their own schemes for delivering large
     * messages in smaller parts through this API. These schemes may or may not
     * bear a relationship to the underlying websocket dataframes in which the
     * message is received off the wire.
     * 
     * @param <T>
     *            The type of the object that represent pieces of the incoming
     *            message that this MessageHandler will consume.
     */
    interface Async<T> extends MessageHandler {

	/**
	 * Called when the next part of a message has been fully received.
	 * 
	 * @param partialMessage
	 *            the partial message data.
	 * @param last
	 *            flag to indicate if this partialMessage is the last of the
	 *            whole message being delivered.
	 */
	void onMessage(T partialMessage, boolean last);
    }

    /**
     * This kind of handler is notified by the container on arrival of a
     * complete message. If the message is received in parts, the container
     * buffers it until it is has been fully received before this method is
     * called. <br/>
     * <br/>
     * For handling incoming text messages, the allowed types for T are <br/>
     * <ul>
     * <li>{@link java.lang.String}</li>
     * <li>{@link java.io.Reader}</li>
     * <li>any developer object for which there is a corresponding
     * {@link Decoder.Text} or {@link Decoder.TextStream} configured</li>
     * </ul>
     * <br/>
     * For handling incoming binary messages, the allowed types for T are <br/>
     * <ul>
     * <li>{@link java.nio.ByteBuffer}</li>
     * <li>byte[]</li>
     * <li>{@link java.io.InputStream}</li>
     * <li>any developer object for which there is a corresponding
     * {@link Decoder.Binary} or {@link Decoder.BinaryStream} configured
     * </ul>
     * <br/>
     * For handling incoming pong messages, the type of T is {@link PongMessage}
     * .<br/>
     * <br/>
     * <p/>
     * Developers should not continue to reference message objects of type
     * {@link java.io.Reader}, {@link java.nio.ByteBuffer} or
     * {@link java.io.InputStream} after the completion of the onMessage() call,
     * since they may be recycled by the implementation.
     * 
     * @param <T>
     *            The type of the message object that this MessageHandler will
     *            consume.
     */
    interface Basic<T> extends MessageHandler {

	/**
	 * Called when the message has been fully received.
	 * 
	 * @param message
	 *            the message data.
	 */
	void onMessage(T message);
    }
}
