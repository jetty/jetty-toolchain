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
 * The ServerContainer is an implementation provided object that, in addition to
 * being able to initiate web socket connections (client), can register
 * endpoints that can handle incoming connection requests.
 * 
 * @since DRAFT 001
 */
public interface ServerContainer extends ClientContainer {
    /**
     * Publish the given endpoint with the provided configuration information.
     * 
     * @param endpointClazz
     *            the class of the endpoint to be deployed.
     */
    void publishServer(Class<? extends Endpoint> endpointClazz);
}
