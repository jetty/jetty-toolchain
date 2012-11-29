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
 */
public interface ClientContainer {
    /**
     * Connect the supplied annotated object to its server using the supplied
     * handshake parameters. The supplied object must be a class decorated with
     * the class level {@link WebSocketEndpoint} annotation.
     * 
     * @param endpoint
     *            either subclass of {@link Endpoint} or a POJO annotated with
     *            {@link WebSocketClient} annotation.
     * @param path
     *            the complete path to the server endpoint
     */
    void connectToServer(Object endpoint, URI path) throws DeploymentException;

    /**
     * Return a copy of the Set of the currently active web socket sessions.
     * These sessions may not still be active at any point after the return of
     * this method, for example, Iterating over the set at a later time may
     * yield closed session. Use session.isActive() to check.
     * 
     * @return the set of sessions, active at the time of return.
     */
    Set<Session> getActiveSessions();

    /**
     * Return the set of Extensions installed in the container.
     * 
     * @return the set of extensions.
     */
    Set<String> getInstalledExtensions();

    /**
     * Returns the maximum size of binary message that this container will
     * buffer.
     * 
     * @return the maximum size of binary message in number of bytes
     */
    long getMaxBinaryMessageBufferSize();

    /**
     * Return the maximum time in milliseconds that a web socket session may be
     * idle before the container may close it.
     * 
     * @return the number of milliseconds idle web socket sessions are active
     */
    long getMaxSessionIdleTimeout();

    /**
     * Sets the maximum size of text message that this container will buffer.
     * 
     * @return the number of seconds idle web socket sessions are active
     */
    long getMaxTextMessageBufferSize();

    /**
     * Sets the maximum size of binary message that this container will buffer.
     * 
     * @param max
     *            the maximum size of binary message in number of bytes
     */
    void setMaxBinaryMessageBufferSize(long max);

    /**
     * Sets the maximum time that a web socket session may be idle before the
     * container may close it.
     * 
     * @param timeout
     *            the maximum time in milliseconds
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
