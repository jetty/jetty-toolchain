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

package javax.websocket.server;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * The handshake request represents the web socket defined Http request that for the opening handshake of a web socket session.
 * 
 * @see DRAFT 013
 */
public interface HandshakeRequest
{
    /**
     * The Sec-WebSocket-Key header name
     */
    static String SEC_WEBSOCKET_KEY = "Sec-WebSocket-Key";
    /**
     * The Sec-WebSocket-Protocol header name
     */
    static String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
    /**
     * The Sec-WebSocket-Version header name
     */
    static String SEC_WEBSOCKET_VERSION = "Sec-WebSocket-Version";
    /**
     * The Sec-WebSocket-Extensions header name
     */
    static String SEC_WEBSOCKET_EXTENSIONS = "Sec-WebSocket-Extensions";

    /**
     * Return the read only Map of Http Headers that came with the handshake request. The header names are case insensitive.
     * 
     * @return the list of headers.
     */
    Map<String, List<String>> getHeaders();

    /**
     * Return a reference to the HttpSession that the web socket handshake that started this conversation was part of, if applicable.
     * 
     * @return the http session.
     */
    Object getHttpSession();

    /**
     * Return the request parameters associated with the request.
     * 
     * @return the unmodifiable map of the request parameters.
     */
    Map<String, List<String>> getParameterMap();

    /**
     * Return the query string associated with the request.
     * 
     * @return the query string≥
     */
    String getQueryString();

    /**
     * Return the request URI of the handshake request.
     * 
     * @return the request uri of the handshake request.
     */
    URI getRequestURI();

    /**
     * Return the authenticated user or null if no user is authenticated for this handshake.
     * 
     * @return the user principal.
     */
    Principal getUserPrincipal();

    /**
     * Checks whether the current user is in the given role.
     * 
     * @param role
     *            the role being checked
     * @return whether the user is in the role
     */
    boolean isUserInRole(String role);
}
