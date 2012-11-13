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
 * The Web Socket Endpoint represents and object that can handle web socket
 * conversations. If deployed as a server, that is to say, the endpoint is
 * registered to a URL, the endpoint may handle one or more web socket
 * conversations, one for each client that establishes a connection. If deployed
 * as a client, the endpoint will participate in only one conversation: that
 * with the server to which it connects. If the endpoint is a server which will
 * cater to multiple clients, the endpoint may be called by multiple threads, no
 * more than one per client, at any one time. This means that when
 * implementing/overriding the methods of Endpoint, the developer should be
 * aware that any state management must be carefully synchronized with this in
 * mine.
 * 
 * @since DRAFT 001
 */
public abstract class Endpoint {
    public Endpoint() {
	/* default constructor */
    }

    /**
     * Developers may implement this method to be notified when an active
     * conversation has just been terminated.
     */
    public void onClose(Session session, CloseReason closeReason) {
	return;
    }

    /**
     * Developers may implement this method when a web socket connection,
     * represented by the session, creates some kind of error that is not
     * modeled in the web socket protocol. This may for example be a
     * notification that an incoming message is too big to handle, or that the
     * incoming message could not be encoded.
     * <p>
     * There are a number of categories of exception that this method is
     * (currently) defined to handle:-
     * <ul>
     * <li>connection problems, for example, a socket failure that occurs before
     * the web socket connection can be formally closed.</li>
     * <li>errors thrown by developer create message handlers calls.</li>
     * <li>conversion errors encoding incoming messages before any message
     * handler has been called.</li>
     * </ul>
     * TBD We may come up with less of a 'catch-all' mechanism for handling
     * exceptions, especially given the varying nature of these categories of
     * exception.
     */
    public void onError(Throwable thr, Session s) {
	return;
    }

    /**
     * Developers may implement this method to be notified when a new
     * conversation has just begun.
     */
    public abstract void onOpen(Session session);
}
