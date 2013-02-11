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
 * The Web Socket Endpoint represents an object that can handle a web socket
 * conversations.
 * <p>
 * When deployed as a server endpoint, that is to say, the endpoint is
 * registered to a URL, the server instantiates a new endpoint instance for each
 * client connection.
 * <p>
 * If deployed as a client, the endpoint will be instantiated once per single
 * connection to the server.
 * <p>
 * If the endpoint is a server which will cater to multiple clients, each
 * endpoint instance corresponding to each active client is called by no more
 * than one thread at a time.
 * <p>
 * This means that when implementing/overriding the methods of Endpoint, the
 * developer is guaranteed that there will be at most one thread in each
 * endpoint instance.
 * 
 * @since DRAFT 001
 * @see DRAFT 012
 */
public abstract class Endpoint {

    /**
     * This method is called immediately prior to the session with the remote
     * peer being closed. This method is called whether the session is being
     * closed because the remote peer initiated a close and sent a close frame,
     * or whether the local websocket container or this endpoint requests to
     * close the session.
     * 
     * @param session
     *            the session about to be closed.
     * @param closeReason
     *            the reason the session was closed.
     */
    public void onClose(Session session, CloseReason closeReason) {
    }

    /**
     * Developers may implement this method when the web socket session creates
     * some kind of error that is not modeled in the web socket protocol. This
     * may for example be a notification that an incoming message is too big to
     * handle, or that the incoming message could not be encoded.<br>
     * <br>
     * There are a number of categories of exception that this method is
     * (currently) defined to handle:-<br>
     * - connection problems, for example, a socket failure that occurs before
     * the web socket connection can be formally closed.<br>
     * - errors thrown by developer create message handlers calls.<br>
     * - conversion errors encoding incoming messages before any message handler
     * has been called.<br>
     * 
     * @param session
     *            the session in use when the error occurs.
     * @param thr
     *            the throwable representing the problem.
     */
    public void onError(Session session, Throwable thr) {
    }

    /**
     * Developers must implement this method to be notified when a new
     * conversation has just begun.
     * 
     * @param session
     *            the session that has just been activated.
     * @param config
     *            the configuration used to configure this endpoint.
     */
    public abstract void onOpen(Session session, EndpointConfiguration config);
}
