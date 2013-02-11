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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The DefaultClientConfiguration is a concrete implementation of a client
 * configuration. Developers may subclass this class in order to provide their
 * own custom configuration behaviors.
 * 
 * @see DRAFT 012
 */
public class DefaultClientConfiguration implements ClientEndpointConfiguration {
    private List<String> preferredSubprotocols = Collections
	    .unmodifiableList(new ArrayList<String>());
    private List<Extension> extensions = Collections
	    .unmodifiableList(new ArrayList<Extension>());
    private List<Encoder> encoders = Collections
	    .unmodifiableList(new ArrayList<Encoder>());
    private List<Decoder> decoders = Collections
	    .unmodifiableList(new ArrayList<Decoder>());

    /**
     * Creates a client configuration with no preferred sub protocols,
     * extensions, decoders or encoders.
     */
    public DefaultClientConfiguration() {
    }

    /**
     * The default implementation of this method performs no actions on the
     * HandshakeResponse.
     * 
     * @param hr
     *            the handshake response sent by the server.
     */
    @Override
    public void afterResponse(HandshakeResponse hr) {
    }

    /**
     * The default implementation of this method performs no actions on the
     * HandshakeRequest headers.
     * 
     * @param hr
     *            handshake request the implementation has formulated.
     */
    @Override
    public void beforeRequest(Map<String, List<String>> hr) {
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
     * Assign the list of decoders this client will use.
     * 
     * @param decoders
     *            the extensions, cannot be null.
     * @return this endpoint configuration.
     */
    public DefaultClientConfiguration setDecoders(List<Decoder> decoders) {
	this.decoders = Collections.unmodifiableList(decoders);
	return this;
    }

    /**
     * Assign the list of encoders this client will use.
     * 
     * @param encoders
     *            the encoders to use, cannot be null.
     * @return this endpoint configuration.
     */
    public DefaultClientConfiguration setEncoders(List<Encoder> encoders) {
	this.encoders = Collections.unmodifiableList(encoders);
	return this;
    }

    /**
     * Assign the List of preferred extensions that this client would like to
     * use.
     * 
     * @param extensions
     *            the extensions, cannot be null.
     * @return this endpoint configuration.
     */
    public DefaultClientConfiguration setExtensions(List<Extension> extensions) {
	this.extensions = Collections.unmodifiableList(extensions);
	return this;
    }

    /**
     * Assign the List of preferred subprotocols that this client would like to
     * use.
     * 
     * @param preferredSubprotocols
     *            the preferred subprotocols.
     * @return this endpoint configuration.
     */
    public DefaultClientConfiguration setPreferredSubprotocols(
	    List<String> preferredSubprotocols) {
	this.preferredSubprotocols = Collections
		.unmodifiableList(preferredSubprotocols);
	return this;
    }
}
