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
 * @see DRAFT 012
 */
public interface RemoteEndpoint {
    /**
     * This method is only used when batching is allowed for this RemoteEndpint.
     * Calling this method forces the implementation to send any unsent messages
     * it has been batching.
     * 
     * @see DRAFT 012
     */
    void flushBatch() throws IOException;

    /**
     * Return the number of milliseconds the implementation will timeout
     * attempting to send a websocket message. A non-positive number indicates
     * the implementation will not timeout attempting to send a websocket
     * message asynchronously. This value overrides the default value assigned
     * in the WebSocketContainer.
     * 
     * @return the timeout time in milliseconds.
     * @see DRAFT 012
     */
    long getAsyncSendTimeout();

    /**
     * Return whether the implementation is allowed to batch outgoing messages
     * before sending. The default mode for RemoteEndpoints is false. The value
     * may be changed by calling {@link #setBatchingAllowed(boolean)
     * setBatchingAllowed}.
     * 
     * @see DRAFT 012
     */
    boolean getBatchingAllowed();

    /**
     * Opens an output stream on which a binary message may be sent. The
     * developer must close the output stream in order to indicate that the
     * complete message has been placed into the output stream.
     * 
     * @return the output stream to which the message will be written.
     * @throws IOException
     *             if there is a problem obtaining the OutputStream to write the
     *             binary message.
     * @see DRAFT 012
     */
    OutputStream getSendStream() throws IOException;

    /**
     * Opens an character stream on which a text message may be sent. The
     * developer must close the writer in order to indicate that the complete
     * message has been placed into the character stream.
     * 
     * @return the writer to which the message will be written.
     * @throws IOException
     *             if there is a problem obtaining the Writer to write the text
     *             message.
     * @see DRAFT 012
     */
    Writer getSendWriter() throws IOException;

    /**
     * Send a binary message, returning when all of the message has been
     * transmitted.
     * 
     * @param data
     *            the message to be sent.
     * @throws IOException
     *             if there is a problem delivering the message.
     * @see DRAFT 012
     */
    void sendBytes(ByteBuffer data) throws IOException;

    /**
     * Initiates the asynchronous transmission of a binary message. This method
     * returns before the message is transmitted. Developers provide a callback
     * to be notified when the message has been transmitted. Errors in
     * transmission are given to the developer in the SendResult object.
     * 
     * @param data
     *            the data being sent.
     * @param completion
     *            the handler that will be notified of progress.
     * @see DRAFT 012
     */
    void sendBytesByCompletion(ByteBuffer data, SendHandler completion);

    /**
     * Initiates the asynchronous transmission of a binary message. This method
     * returns before the message is transmitted. Developers use the returned
     * Future object to track progress of the transmission. Errors in
     * transmission are given to the developer in the SendResult object.
     * 
     * @param data
     *            the data being sent.
     * @return the Future object representing the send operation.
     * @see DRAFT 012
     */
    Future<SendResult> sendBytesByFuture(ByteBuffer data);

    /**
     * Sends a custom developer object, blocking until it has been transmitted.
     * Containers will by default be able to encode java primitive types, their
     * object equivalents, and arrays or collections thereof. The developer will
     * have provided an encoder for this object type in the endpoint
     * configuration.
     * 
     * @param o
     *            the object to be sent.
     * @throws IOException
     *             if there is a communication error sending the message object.
     * @throws EncodeException
     *             if there was a problem encoding the message object into the
     *             form of a native websocket message.
     * @see DRAFT 012
     */
    void sendObject(Object o) throws IOException, EncodeException;

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
     * @param handler
     *            the handler that will be notified of progress.
     * @see DRAFT 012
     */
    void sendObjectByCompletion(Object o, SendHandler handler);

    /**
     * Initiates the asynchronous transmission of a custom developer object. The
     * developer will have provided an encoder for this object type in the
     * endpoint configuration. Containers will by default be able to encode java
     * primitive types, their object equivalents, and arrays or collections
     * thereof. Progress is be tracked using the Future object.
     * 
     * @param o
     *            the object being sent.
     * @return the Future object representing the send operation.
     * @see DRAFT 012
     */
    Future<SendResult> sendObjectByFuture(Object o);

    /**
     * Send a binary message in parts, blocking until all of the message has
     * been transmitted. The runtime reads the message in order. Non-final parts
     * are sent with isLast set to false. The final piece must be sent with
     * isLast set to true.
     * 
     * @param partialByte
     *            the part of the message being sent.
     * @param isLast
     *            Whether the partial message being sent is the last part of the
     *            message.
     * @throws IOException
     *             if there is a problem delivering the partial message.
     * @see DRAFT 012
     */
    void sendPartialBytes(ByteBuffer partialByte, boolean isLast)
	    throws IOException; // or Iterable<byte[]>

    /**
     * Send a text message in parts, blocking until all of the message has been
     * transmitted. The runtime reads the message in order. Non-final parts of
     * the message are sent with isLast set to false. The final part must be
     * sent with isLast set to true.
     * 
     * @param partialMessage
     *            the parts of the message being sent.
     * @param isLast
     *            Whether the partial message being sent is the last part of the
     *            message.
     * @throws IOException
     *             if there is a problem delivering the message fragment.
     * @see DRAFT 012
     */
    void sendPartialString(String partialMessage, boolean isLast)
	    throws IOException;

    /**
     * Send a Ping message containing the given application data to the remote
     * endpoint. The corresponding Pong message may be picked up using the
     * MessageHandler.Pong handler.
     * 
     * @param applicationData
     *            the data to be carried in the ping request.
     * @throws IOException
     *             if the ping failed to be sent
     * @throws IllegalArgumentException
     *             if the applicationData exceeds the maximum allowed payload of
     *             125 bytes
     * @see DRAFT 012
     */
    void sendPing(ByteBuffer applicationData) throws IOException,
	    IllegalArgumentException;

    /**
     * Allows the developer to send an unsolicited Pong message containing the
     * given application data in order to serve as a unidirectional heartbeat
     * for the session.
     * 
     * @param applicationData
     *            the application data to be carried in the pong response.
     * @throws IOException
     *             if the pong failed to be sent
     * @throws IllegalArgumentException
     *             if the applicationData exceeds the maximum allowed payload of
     *             125 bytes
     * @see DRAFT 012
     */
    void sendPong(ByteBuffer applicationData) throws IOException,
	    IllegalArgumentException;

    /**
     * Send a text message, blocking until all of the message has been
     * transmitted.
     * 
     * @param text
     *            the message to be sent.
     * @throws IOException
     *             if there is a problem delivering the message.
     * @see DRAFT 012
     */
    void sendString(String text) throws IOException;

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
     * @see DRAFT 012
     */
    void sendStringByCompletion(String text, SendHandler completion);

    /**
     * Initiates the asynchronous transmission of a text message. This method
     * returns before the message is transmitted. Developers use the returned
     * Future object to track progress of the transmission. Errors in
     * transmission are given to the developer in the SendResult object.
     * 
     * @param text
     *            the text being sent.
     * @return the Future object representing the send operation.
     * @see DRAFT 012
     */
    Future<SendResult> sendStringByFuture(String text);

    /**
     * Sets the number of milliseconds the implementation will timeout
     * attempting to send a websocket message. A non-positive number indicates
     * the implementation will not timeout attempting to send a websocket
     * message asynchronously. This value overrides the default value assigned
     * in the WebSocketContainer.
     * 
     * @param timeoutmillis
     *            The number of milliseconds this RemoteEndpoint will wait
     *            before timing out an incomplete asynchronous message send.
     * @see DRAFT 012
     */
    void setAsyncSendTimeout(long timeoutmillis);

    /**
     * Indicate to the implementation that it is allowed to batch outgoing
     * messages before sending. Not all implementations support batching of
     * outgoing messages. The default mode for RemoteEndpoints is false. If the
     * developer has indicated that batching of outgoing messages is permitted,
     * then the developer must call flushBatch() in order to be sure that all
     * the messages passed into the send methods of this RemoteEndpoint are
     * sent. If batching is allowed, if the developer has called send methods on
     * this RemoteEndpoint without calling flushBatch(), then the implementation
     * may not have sent all the messages the developer has asked to be sent. If
     * the parameter value is false and the implementation has a batch of unsent
     * messages, then the implementation must immediately send the batch of
     * unsent messages.
     * 
     * @param allowed
     *            whether the implementation is allowed to batch messages.
     * @see DRAFT 012
     */
    void setBatchingAllowed(boolean allowed);
}
