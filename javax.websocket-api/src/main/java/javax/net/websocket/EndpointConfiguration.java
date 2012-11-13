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

/**
 * The endpoint configuration contains all the information needed during the
 * handshake process for this end point. All endpoints specify, for example, a
 * URI. In the case of a server endpoint, the URI signifies the URI to which the
 * endpoint will be mapped. In the case of a client application the URI
 * signifies the URI of the server to which the client endpoint will attempt to
 * connect.
 * 
 * @since DRAFT 001
 */
public interface EndpointConfiguration {
    /**
     * Return the Decoder implementations configured. These will be used by the
     * container to decode incoming messages into the expected custom objects on
     * MessageListener.onMessage() callbacks.
     * 
     * @return the list of decoders.
     */
    List<Decoder> getDecoders();

    /**
     * Return the Encoder implementations configured. These will be used by the
     * container to encode custom objects passed into the send() methods on
     * remote endpoints.
     * 
     * @return the list of encoders.
     */
    List<Encoder> getEncoders();

    /**
     * http://java.net/jira/browse/WEBSOCKET_SPEC-46
     */
    String getPath();
}
