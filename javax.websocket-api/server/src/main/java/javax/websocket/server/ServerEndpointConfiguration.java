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

import java.util.List;

import javax.websocket.EndpointConfiguration;
import javax.websocket.Extension;

/**
 * The ServerEndpointConfiguration is a special kind of endpoint configuration object that contains web socket configuration information specific only to server
 * endpoints. For developers deploying programmatic endpoints, ServerEndpointConfiguration objects can be created using a
 * {@link ServerEndpointConfigurationBuilder}.
 * 
 * @see DRAFT 013
 */
public interface ServerEndpointConfiguration extends EndpointConfiguration
{

    /**
     * Returns the Class of the endpoint this configuration is configuring. If the endpoint is an annotated endpoint, the value is the class of the Java class
     * annotated with @ServerEndpoint. if the endpoint is a programmatic, the value is the class of the subclass of Endpoint.
     * 
     * @return the class of the endpoint, annotated or programmatic.
     */
    Class<?> getEndpointClass();

    /**
     * Return the websocket extensions configured.
     * 
     * @return the list of extensions, the empty list if none.
     */
    List<Extension> getExtensions();

    /**
     * Return the path for this endpoint configuration. The path is the URI or URI-template relative to the websocket root of the server to which the endpoint
     * using this configuration will be mapped. The path is always non-null and always begins with a leading "/".
     * 
     * @return the relative path for this configuration.
     */
    String getPath();

    /**
     * Return the {@link ServerEndpointConfigurator} this configuration is using. If none was set by calling
     * {@link ServerEndpointConfigurationBuilder#serverEndpointConfigurator(javax.websocket.server.ServerEndpointConfigurator) } this methods returns the
     * platform default configurator.
     * 
     * @return the configurator in use.
     */
    ServerEndpointConfigurator getServerEndpointConfigurator();

    /**
     * Return the websocket subprotocols configured.
     * 
     * @return the list of subprotocols, the empty list if none
     */
    List<String> getSubprotocols();
}
