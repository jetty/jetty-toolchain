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
import java.net.URI;
import java.util.Set;

/**
 * A WebSocketContainer is an implementation provided object that provides
 * applications a view on the container running it. The WebSocketContainer
 * container various configuration parameters that control default session and
 * buffer properties of the endpoints it contains. It also allows the developer
 * to deploy websocket client endpoints by initiating a web socket handshake
 * from the provided endpoint to a supplied URI where the peer endpoint is
 * presumed to reside. <br>
 * <br>
 * A WebSocketContainer may be accessed by concurrent threads, so
 * implementations must ensure the integrity of its mutable attributes in such
 * circumstances.
 * 
 * @see DRAFT 013
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
     *             if the configuration is not valid
     * @throws IOException
     *             if there was a network or protocol problem that prevented the
     *             client endpoint being connected to its server
     */
    Session connectToServer(Class<? extends Endpoint> endpointClass,
	    ClientEndpointConfiguration cec, URI path)
	    throws DeploymentException, IOException;

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
     *             if the annotated endpoint class is not valid.
     * @throws IOException
     *             if there was a network or protocol problem that prevented the
     *             client endpoint being connected to its server.
     */
    Session connectToServer(Class<?> annotatedEndpointClass, URI path)
	    throws DeploymentException, IOException;

    /**
     * Return the number of milliseconds the implementation will timeout
     * attempting to send a websocket message for all RemoteEndpoints associated
     * with this container. A non-positive number indicates the implementation
     * will not timeout attempting to send a websocket message asynchronously.
     * Note this default may be overridden in each RemoteEndpoint.
     * 
     * @return the timeout time in millsenconds.
     */
    long getDefaultAsyncSendTimeout();

    /**
     * Returns the default maximum size of incoming binary message that this
     * container will buffer. This default may be overridden on a per session
     * basis using {@link Session#setMaxBinaryMessageBufferSize(int) }
     * 
     * @return the maximum size of incoming binary message in number of bytes.
     */
    int getDefaultMaxBinaryMessageBufferSize();

    /**
     * Return the default time in milliseconds after which any web socket
     * sessions in this container will be closed if it has been inactive. A
     * value that is 0 or negative indicates the sessions will never timeout due
     * to inactivity. The value may be overridden on a per session basis using
     * {@link Session#setMaxIdleTimeout(long) }
     * 
     * @return the default number of milliseconds after which an idle session in
     *         this container will be closed
     */
    long getDefaultMaxSessionIdleTimeout();

    /**
     * Returns the default maximum size of incoming text message that this
     * container will buffer. This default may be overridden on a per session
     * basis using {@link Session#setMaxTextMessageBufferSize(int) }
     * 
     * @return the maximum size of incoming text message in number of bytes.
     */
    int getDefaultMaxTextMessageBufferSize();

    /**
     * Return the set of Extensions installed in the container.
     * 
     * @return the set of extensions.
     */
    Set<Extension> getInstalledExtensions();

    /**
     * Sets the number of milliseconds the implementation will timeout
     * attempting to send a websocket message for all RemoteEndpoints associated
     * with this container. A non-positive number indicates the implementation
     * will not timeout attempting to send a websocket message asynchronously.
     * Note this default may be overridden in each RemoteEndpoint.
     */
    void setAsyncSendTimeout(long timeoutmillis);

    /**
     * Sets the default maximum size of incoming binary message that this
     * container will buffer.
     * 
     * @param max
     *            the maximum size of binary message in number of bytes.
     */
    void setDefaultMaxBinaryMessageBufferSize(int max);

    /**
     * Sets the default time in milliseconds after which any web socket sessions
     * in this container will be closed if it has been inactive. A value that is
     * 0 or negative indicates the sessions will never timeout due to
     * inactivity. The value may be overridden on a per session basis using
     * {@link Session#setMaxIdleTimeout(long) }
     * 
     * @param timeout
     *            the maximum time in milliseconds.
     */
    void setDefaultMaxSessionIdleTimeout(long timeout);

    /**
     * Sets the maximum size of incoming text message that this container will
     * buffer.
     * 
     * @param max
     *            the maximum size of text message in number of bytes.
     */
    void setDefaultMaxTextMessageBufferSize(int max);
}
