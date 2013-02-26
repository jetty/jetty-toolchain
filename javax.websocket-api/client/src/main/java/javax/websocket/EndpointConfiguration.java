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
 * The endpoint configuration contains all the information needed during the
 * handshake process for this end point. All endpoints specify, for example, a
 * URI. In the case of a server endpoint, the URI signifies the URI to which the
 * endpoint will be mapped. In the case of a client application the URI
 * signifies the URI of the server to which the client endpoint will attempt to
 * connect.
 * 
 * @see DRAFT 013
 */
public interface EndpointConfiguration {
    /**
     * Return the Decoder implementations configured. These will be used by the
     * container to decode incoming messages into the expected custom objects on
     * {@link MessageHandler.Basic#onMessage(Object)} callbacks.
     * 
     * @return the decoders, the empty list if none.
     */
    List<Decoder> getDecoders();

    /**
     * Return the Encoder implementations configured. These will be used by the
     * container to encode custom objects passed into the send() methods on
     * remote endpoints.
     * 
     * @return the encoders, an empty list if none.
     */
    List<Encoder> getEncoders();

    /**
     * This method returns a modifiable Map that the developer may use to store
     * application specific information relating to the endpoint that uses this
     * configuration instance. Web socket applications running on distributed
     * implementations of the web container should make any application specific
     * objects stored here java.io.Serializable, or the object may not be
     * recreated after a failover.
     * 
     * @return a modifiable Map of application data.
     */
    Map<String, Object> getUserProperties();
}
