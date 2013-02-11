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

import java.net.URI;
import java.util.Set;

/**
 * A ClientContainer is an implementation provided object that allows the
 * developer to initiate a web socket handshake from the provided endpoint.
 * 
 * @since DRAFT 001
 * @see DRAFT 012
 */
public interface WebSocketContainer {
    /**
     * Connect the supplied programmatic endpoint to its server with the given
     * configuration. This method blocks until the connection is established, or
     * throws an error if the connection could not be made.
     * 
     * @param endpointClass
     *            the programmatic client endpoint class {@link Endpoint}.
     * @param path
     *            the complete path to the server endpoint.
     * @param cec
     *            the configuration used to configure the programmatic endpoint.
     * @return the Session created if the connection is successful.
     * @throws DeploymentException
     *             if there was a problem that prevented the client endpoint
     *             being connected to its server.
     * @see DRAFT 012
     */
    Session connectToServer(Class<? extends Endpoint> endpointClass,
	    ClientEndpointConfiguration cec, URI path)
	    throws DeploymentException;

    /**
     * Connect the supplied annotated object to its server. The supplied object
     * must be a class decorated with the class level
     * {@link javax.websocket.server.WebSocketEndpoint
     * javax.websocket.server.WebSocketEndpoint} annotation. This method blocks
     * until the connection is established, or throws an error if either the
     * connection could not be made or there was a problem with the supplied
     * endpoint class.
     * 
     * @param annotatedEndpointClass
     *            the annotated websocket client endpoint with
     *            {@link WebSocketClient} annotation.
     * @param path
     *            the complete path to the server endpoint.
     * @return the Session created if the connection is successful.
     * @throws DeploymentException
     *             if there was a problem that prevented the client endpoint
     *             being connected to its server.
     * @see DRAFT 012
     */
    Session connectToServer(Class<?> annotatedEndpointClass, URI path)
	    throws DeploymentException;

    /**
     * Return the number of milliseconds the implementation will timeout
     * attempting to send a websocket message for all RemoteEndpoints associated
     * with this container. A non-positive number indicates the implementation
     * will not timeout attempting to send a websocket message asynchronously.
     * Note this default may be overridden in each RemoteEndpoint.
     * 
     * @return the timeout time in millsenconds.
     * @see DRAFT 012
     */
    long getDefaultAsyncSendTimeout();

    /**
     * Returns the default maximum size of incoming binary message that this
     * container will buffer. This default may be overridden on a per session
     * basis using {@link Session#setMaxBinaryMessageBufferSize(int) }
     * 
     * @return the maximum size of incoming binary message in number of bytes.
     * @see DRAFT 012
     */
    int getDefaultMaxBinaryMessageBufferSize();

    /**
     * Returns the default maximum size of incoming text message that this
     * container will buffer. This default may be overridden on a per session
     * basis using {@link Session#setMaxTextMessageBufferSize(int) }
     * 
     * @return the maximum size of incoming text message in number of bytes.
     * @see DRAFT 012
     */
    int getDefaultMaxTextMessageBufferSize();

    /**
     * Return the set of Extensions installed in the container.
     * 
     * @return the set of extensions.
     * @see DRAFT 012
     */
    Set<String> getInstalledExtensions();

    /**
     * Return the maximum time in milliseconds that a web socket session may be
     * idle before the container may close it.
     * 
     * @return the number of milliseconds idle web socket sessions are active
     * @see DRAFT 012
     */
    long getMaxSessionIdleTimeout();

    /**
     * Sets the number of milliseconds the implementation will timeout
     * attempting to send a websocket message for all RemoteEndpoints associated
     * with this container. A non-positive number indicates the implementation
     * will not timeout attempting to send a websocket message asynchronously.
     * Note this default may be overridden in each RemoteEndpoint.
     * 
     * @see DRAFT 012
     */
    void setAsyncSendTimeout(long timeoutmillis);

    /**
     * Sets the default maximum size of incoming binary message that this
     * container will buffer.
     * 
     * @param max
     *            the maximum size of binary message in number of bytes.
     * @see DRAFT 012
     */
    void setDefaultMaxBinaryMessageBufferSize(int max);

    /**
     * Sets the maximum size of incoming text message that this container will
     * buffer.
     * 
     * @param max
     *            the maximum size of text message in number of bytes.
     * @see DRAFT 012
     */
    void setDefaultMaxTextMessageBufferSize(int max);

    /**
     * Sets the maximum time that a web socket session may be idle before the
     * container may close it.
     * 
     * @param timeout
     *            the maximum time in milliseconds
     * @see DRAFT 012
     */
    void setMaxSessionIdleTimeout(long timeout);

    /**
     * Sets the maximum size of text message that this container will buffer.
     * 
     * @param max
     *            the maximum size of text message in number of bytes
     */
    void setMaxTextMessageBufferSize(long max);
}
