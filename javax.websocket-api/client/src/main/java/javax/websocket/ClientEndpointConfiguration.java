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

import java.util.List;
import java.util.Map;

/**
 * The ClientEndpointConfiguration is a special kind of endpoint configuration
 * object that contains web socket configuration information specific only to
 * client endpoints.
 * 
 * @since DRAFT 001
 * @see DRAFT 012
 */
public interface ClientEndpointConfiguration extends EndpointConfiguration {

    /**
     * This method is called by the implementation after it has received a
     * handshake response from the server as a result of a handshake interaction
     * it initiated. The developer may implement this method in order to inspect
     * the returning handshake response.
     * 
     * @param hr
     *            the handshake response sent by the server.
     * @see DRAFT 012
     */
    void afterResponse(HandshakeResponse hr);

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
     * @see DRAFT 012
     */
    void beforeRequest(Map<String, List<String>> headers);

    /**
     * http://java.net/jira/browse/WEBSOCKET_SPEC-45
     * <p>
     * Return the list of all the extensions that this client supports, the
     * empty list if there are none. These are the extensions that will be used
     * to populate the Sec-WebSocket-Extensions header in the opening handshake
     * for clients using this configuration. The first extension in the list is
     * the most preferred extension. See <a
     * href="http://tools.ietf.org/html/rfc6455#section-9.1">Negotiating
     * Extensions</a>
     * 
     * @return a list of extensions
     * @see DRAFT 012
     */
    List<Extension> getExtensions();

    /**
     * The ordered list of sub protocols a client endpoint would like to use,
     * the empty list if there are none. This list is used to generate the
     * Sec-WebSocket-Protocol header in the opening handshake for clients using
     * this configuration. The first protocol name is the most preferred. See <a
     * href="http://tools.ietf.org/html/rfc6455#section-4.1">Client Opening
     * Handshake</a>
     * 
     * @return a list of preferred subprotocols
     * @see DRAFT 012
     */
    List<String> getPreferredSubprotocols();
}
