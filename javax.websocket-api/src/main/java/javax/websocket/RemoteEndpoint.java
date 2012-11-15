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
import java.util.concurrent.Future;

/**
 * The RemoteEndpoint object is supplied by the container and represents the
 * 'other end' of the Web Socket conversation. In particular, objects of this
 * kind include numerous ways to send web socket messages. There is no guarantee
 * of the success of receipt of a web socket message, but if the action of
 * sending a message causes a known error, the API throws it. This object
 * includes a variety of ways to send messages to the other end of a web socket
 * session: by whole message, in pieces and asynchronously, where the point of
 * completion is defined when all the supplied data had been written to the
 * underlying connection. The completion handlers for the asynchronous methods
 * are always called with a different thread from that which initiated the send.
 * 
 * @since DRAFT 001
 */
public interface RemoteEndpoint {
    /**
     * Send a text message, blocking until all of the message has been
     * transmitted.
     * 
     * @param text
     *            the message to be sent
     */
    void sendString(String text) throws IOException;

    /**
     * Send a binary message, returning when all of the message has been
     * transmitted.
     * 
     * @param data
     *            the message to be sent
     */
    void sendBytes(ByteBuffer data) throws IOException;

    /**
     * Send a text message in pieces, blocking until all of the message has been
     * transmitted. The runtime reads the message in order. Non-final pieces are
     * sent with isLast set to false. The final piece must be sent with isLast
     * set to true.
     * 
     * @param fragment
     *            the piece of the message being sent
     */
    void sendPartialString(String fragment, boolean isLast) throws IOException;

    /**
     * Send a binary message in pieces, blocking until all of the message has
     * been transmitted. The runtime reads the message in order. Non-final
     * pieces are sent with isLast set to false. The final piece must be sent
     * with isLast set to true.
     * 
     * @param partialByte
     *            the piece of the message being sent
     */
    void sendPartialBytes(ByteBuffer partialByte, boolean isLast)
	    throws IOException;

    /**
     * Opens an output stream on which a binary message may be sent. The
     * developer must close the output stream in order to indicate that the
     * complete message has been placed into the output stream.
     * 
     * @return the output stream to which the message will be written
     */
    OutputStream getSendStream() throws IOException;

    /**
     * Opens an character stream on which a text message may be sent. The
     * developer must close the writer in order to indicate that the complete
     * message has been placed into the character stream.
     * 
     * @return the output stream to which the message will be written
     */
    Writer getSendWriter() throws IOException;

    /**
     * Sends a custom developer object, blocking until it has been transmitted.
     * Containers will by default be able to encode java primitive types, their
     * object equivalents, and arrays or collections thereof. The developer will
     * have provided an encoder for this object type in the endpoint
     * configuration.
     * 
     * @param o
     *            the object to be sent
     */
    void sendObject(Object o) throws IOException, EncodeException;

    /**
     * Initiates the asynchronous transmission of a text message. This method
     * returns before the message is transmitted. Developers provide a callback
     * to be notified when the message has been transmitted. Errors in
     * transmission are given to the developer in the SendResult object.
     * 
     * @param text
     *            the text being sent.
     * @param completion
     *            the handler which will be notified of progress.
     * @return
     */
    void sendStringByCompletion(String text, SendHandler completion);

    /**
     * Initiates the asynchronous transmission of a text message. This method
     * returns before the message is transmitted. Developers may provide a
     * callback to be notified when the message has been transmitted, or may use
     * the returned Future object to track progress of the transmission. Errors
     * in transmission are given to the developer in the SendResult object in
     * either case.
     * 
     * @param text
     *            the text being sent
     * @param completion
     *            the handler which will be notified of progress
     */
    Future<SendResult> sendStringByFuture(String text);

    /**
     * Initiates the asynchronous transmission of a binary message. This method
     * returns before the message is transmitted. Developers may provide a
     * callback to be notified when the message has been transmitted, or may use
     * the returned Future object to track progress of the transmission. Errors
     * in transmission are given to the developer in the SendResult object in
     * either case.
     * 
     * @param data
     *            the data being sent
     * @param completion
     *            handler that will be notified of progress
     */
    Future<SendResult> sendBytesByFuture(ByteBuffer data);

    /**
     * Initiates the asynchronous transmission of a binary message. This method
     * returns before the message is transmitted. Developers may provide a
     * callback to be notified when the message has been transmitted, or may use
     * the returned Future object to track progress of the transmission. Errors
     * in transmission are given to the developer in the SendResult object in
     * either case.
     * 
     * @param data
     *            the data being sent
     * @param completion
     *            handler that will be notified of progress
     */
    void sendBytesByCompletion(ByteBuffer data, SendHandler completion);

    /**
     * Initiates the asynchronous transmission of a custom developer object. The
     * developer will have provided an encoder for this object type in the
     * endpoint configuration. Containers will by default be able to encode java
     * primitive types, their object equivalents, and arrays or collections
     * thereof. Progress is be tracked using the Future object.
     * 
     * @param o
     *            the object being sent.
     * @param completion
     *            the handler that will be notified of progress
     * @return future
     */
    Future<SendResult> sendObjectByFuture(Object o);

    /**
     * Initiates the asynchronous transmission of a custom developer object. The
     * developer will have provided an encoder for this object type in the
     * endpoint configuration. Containers will by default be able to encode java
     * primitive types, their object equivalents, and arrays or collections
     * thereof. Developers are notified when transmission is complete through
     * the supplied callback object.
     * 
     * @param o
     *            the object being sent.
     * @param completion
     *            the handler that will be notified of progress
     */
    void sendObjectByCompletion(Object o, SendHandler completion);

    /**
     * Send a Ping message containing the given application data to the remote
     * endpoint. The corresponding Pong message may be picked up using the
     * MessageHandler.Pong handler.
     * 
     * @param applicationData
     *            the data to be carried in the ping request
     */
    void sendPing(ByteBuffer applicationData);

    /**
     * Allows the developer to send an unsolicited Pong message containing the
     * given application data in order to serve as a unidirectional heartbeat
     * for the session.
     * 
     * @param applicationData
     *            the application data to be carried in the pong response.
     */
    void sendPong(ByteBuffer applicationData);
}
