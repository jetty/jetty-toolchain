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

package javax.net.websocket;

import java.util.List;
import java.util.Map;

/**
 * The handshake response represents the web socket defined http response that
 * will be sent by the web socket server during the opening handshake.
 */
// FIXME: needs to be available on client too
public interface HandshakeResponse {
    /**
     * Return the list of Http Headers that came with the handshake request.
     * 
     * @return the headers
     */
    abstract Map<String, List<String>> getHeaders();
}
