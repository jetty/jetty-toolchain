//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
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

import java.util.List;
import java.util.Map;

/**
 * The ClientEndpointConfigurator interface may be implemented by developers who
 * want to provide custom configuration algorithms, such as intercepting the
 * opening handshake, or providing arbitrary methods and algorithms that can be
 * accessed from each endpoint instance configured with this configurator.
 * 
 * @see DRAFT 013
 */
public abstract class ClientEndpointConfigurator {

    /**
     * This method is called by the implementation after it has received a
     * handshake response from the server as a result of a handshake interaction
     * it initiated. The developer may implement this method in order to inspect
     * the returning handshake response.
     * 
     * @param hr
     *            the handshake response sent by the server.
     */
    public void afterResponse(HandshakeResponse hr) {

    }

    /**
     * This method is called by the implementation after it has formulated the
     * handshake request that will be used to initiate the connection to the
     * server, but before it has sent any part of the request. This allows the
     * developer to inspect and modify the handshake request headers prior to
     * the start of the handshake interaction.
     * 
     * @param headers
     *            the mutable map of handshake request headers the
     *            implementation is about to send to start the handshake
     *            interaction.
     */
    public void beforeRequest(Map<String, List<String>> headers) {

    }
}
