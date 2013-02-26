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

/**
 * The ClientEndpointConfiguration is a special kind of endpoint configuration
 * object that contains web socket configuration information specific only to
 * client endpoints.
 * 
 * @see DRAFT 013
 */
public interface ClientEndpointConfiguration extends EndpointConfiguration {

    /**
     * Return the custom configurator for this configuration. If the developer
     * did not provide one, the platform default configurator is returned.
     * 
     * @return the configurator in use with this configuration.
     */
    public ClientEndpointConfigurator getClientEndpointConfigurator();

    /**
     * Return the extensions, in order of preference, favorite first, that this
     * client would like to use for its sessions. These are the extensions that
     * will be used to populate the Sec-WebSocket-Extensions header in the
     * opening handshake for clients using this configuration. The first
     * extension in the list is the most preferred extension. See <a
     * href="http://tools.ietf.org/html/rfc6455#section-9.1">Negotiating
     * Extensions</a>.
     * 
     * @return the list of extensions, the empty list if there are none.
     */
    List<Extension> getExtensions();

    /**
     * Return the ordered list of sub protocols a client endpoint would like to
     * use, in order of preference, favorite first that this client would like
     * to use for its sessions. This list is used to generate the
     * Sec-WebSocket-Protocol header in the opening handshake for clients using
     * this configuration. The first protocol name is the most preferred. See <a
     * href="http://tools.ietf.org/html/rfc6455#section-4.1">Client Opening
     * Handshake</a>.
     * 
     * @return the list of the preferred subprotocols, the empty list if there
     *         are none
     */
    List<String> getPreferredSubprotocols();
}
