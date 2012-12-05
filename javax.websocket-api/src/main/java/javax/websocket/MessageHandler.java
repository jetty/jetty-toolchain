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
 * @since DRAFT 001
 */
public interface MessageHandler {
    /**
     * This kind of listener listens is notified by the container as parts of a
     * message arrive. The allowable types for T are String, ByteBuffer and
     * byte[].
     * 
     * @since DRAFT 002
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
     * called. The allowed types for T are String, ByteBuffer, byte[], Reader,
     * InputStream, PongMessage, and any developer object for which there is a
     * corresponding Decoder configured.
     * 
     * @since DRAFT 002
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
