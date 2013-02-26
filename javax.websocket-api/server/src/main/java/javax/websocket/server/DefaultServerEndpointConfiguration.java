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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Decoder;
import javax.websocket.Encoder;
import javax.websocket.Endpoint;
import javax.websocket.Extension;

/**
 * The DefaultServerConfiguration is a concrete class that embodies all the configuration parameters for an endpoint that is to be published as a server
 * endpoint. Developers may subclass this class in order to override the configuration behavior.
 * 
 * @see DRAFT 013
 */
final class DefaultServerEndpointConfiguration implements ServerEndpointConfiguration
{
    private String path;
    private Class<?> endpointClass;
    private List<String> subprotocols;
    private List<Extension> extensions;
    private List<Encoder> encoders;
    private List<Decoder> decoders;
    private Map<String, Object> userProperties = new HashMap<String, Object>();
    private ServerEndpointConfigurator serverEndpointConfigurator;

    /**
     * Creates a server configuration with the given path
     * 
     * @param path
     *            the URI or URI template.
     */
    DefaultServerEndpointConfiguration(Class<? extends Endpoint> endpointClass, String path)
    {
        this.path = path;
        this.endpointClass = endpointClass;
    }

    // The builder ensures nothing except configurator can be null.
    DefaultServerEndpointConfiguration(Class<?> endpointClass, String path, List<String> subprotocols, List<Extension> extensions, List<Encoder> encoders,
            List<Decoder> decoders, ServerEndpointConfigurator serverEndpointConfigurator)
    {
        this.path = path;
        this.endpointClass = endpointClass;
        this.subprotocols = Collections.unmodifiableList(subprotocols);
        this.extensions = Collections.unmodifiableList(extensions);
        this.encoders = Collections.unmodifiableList(encoders);
        this.decoders = Collections.unmodifiableList(decoders);
        if (serverEndpointConfigurator == null)
        {
            this.serverEndpointConfigurator = ServerEndpointConfigurator.fetchContainerDefaultConfigurator();
        }
        else
        {
            this.serverEndpointConfigurator = serverEndpointConfigurator;
        }
    }

    /**
     * Return the Decoder implementations configured. These will be used by the container to decode incoming messages into the expected custom objects on
     * MessageHandler callbacks.
     * 
     * @return the encoders, in an unmodifiable list.
     */
    @Override
    public List<Decoder> getDecoders()
    {
        return this.decoders;
    }

    /**
     * Return the Encoder implementations configured. These will be used by the container to encode outgoing messages.
     * 
     * @return the encoders, in an unmodifiable list, empty if there are none.
     */
    @Override
    public List<Encoder> getEncoders()
    {
        return this.encoders;
    }

    /**
     * Returns the class of the Endpoint that this configuration configures.
     * 
     * @return the class of the Endpoint.
     */
    @Override
    public Class<?> getEndpointClass()
    {
        return this.endpointClass;
    }

    @Override
    public final List<Extension> getExtensions()
    {
        return this.extensions;
    }

    /**
     * Return the path of this server configuration. The path is a relative URI or URI-template.
     * 
     * @return the path
     */
    @Override
    public String getPath()
    {
        return path;
    }

    /**
     * Return the ServerEndpointConfigurator
     * 
     * @return the ServerEndpointConfigurator
     */
    @Override
    public ServerEndpointConfigurator getServerEndpointConfigurator()
    {
        return this.serverEndpointConfigurator;
    }

    @Override
    public final List<String> getSubprotocols()
    {
        return this.subprotocols;
    }

    /**
     * Editable map of user properties.
     */
    @Override
    public final Map<String, Object> getUserProperties()
    {
        return this.userProperties;
    }

}
