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
 * The handshake response represents the web socket-defined Http response that
 * is the response to the opening handshake request.
 * 
 * @see DRAFT 013
 */
public interface HandshakeResponse {
    /**
     * The Sec-WebSocket-Accept header name.
     */
    static final String SEC_WEBSOCKET_ACCEPT = "Sec-WebSocket-Accept";

    /**
     * Return the list of Http headers sent by the web socket server.
     * 
     * @return the http headers .
     */
    Map<String, List<String>> getHeaders();
}
