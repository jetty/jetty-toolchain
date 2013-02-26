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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DefaultClientConfiguration is a concrete implementation of a client
 * configuration. Developers may subclass this class in order to provide their
 * own custom configuration behaviors.
 * 
 * @see DRAFT 013
 */
final class DefaultClientEndpointConfiguration implements
	ClientEndpointConfiguration {
    private List<String> preferredSubprotocols;
    private List<Extension> extensions;
    private List<Encoder> encoders;
    private List<Decoder> decoders;
    private Map<String, Object> userProperties = new HashMap<String, Object>();
    private ClientEndpointConfigurator clientEndpointConfigurator;

    DefaultClientEndpointConfiguration(List<String> preferredSubprotocols,
	    List<Extension> extensions, List<Encoder> encoders,
	    List<Decoder> decoders,
	    ClientEndpointConfigurator clientEndpointConfigurator) {
	this.preferredSubprotocols = Collections
		.unmodifiableList(preferredSubprotocols);
	this.extensions = Collections.unmodifiableList(extensions);
	this.encoders = Collections.unmodifiableList(encoders);
	this.decoders = Collections.unmodifiableList(decoders);
	this.clientEndpointConfigurator = clientEndpointConfigurator;
    }

    @Override
    public ClientEndpointConfigurator getClientEndpointConfigurator() {
	return this.clientEndpointConfigurator;
    }

    /**
     * Return the (unmodifiable) list of decoders this client will use.
     * 
     * @return the decoders to use.
     */
    @Override
    public List<Decoder> getDecoders() {
	return this.decoders;
    }

    /**
     * Return the (unmodifiable) list of encoders this client will use.
     * 
     * @return the encoder list.
     */
    @Override
    public List<Encoder> getEncoders() {
	return this.encoders;
    }

    /**
     * Return the extensions, in order of preference, favorite first, that this
     * client would like to use for its sessions.
     * 
     * @return the (unmodifiable) extension list.
     */
    @Override
    public List<Extension> getExtensions() {
	return this.extensions;
    }

    /**
     * Return the protocols, in order of preference, favorite first, that this
     * client would like to use for its sessions.
     * 
     * @return the preferred subprotocols.
     */
    @Override
    public List<String> getPreferredSubprotocols() {
	return this.preferredSubprotocols;
    }

    /**
     * Editable map of user properties.
     */
    @Override
    public final Map<String, Object> getUserProperties() {
	return this.userProperties;
    }
}
