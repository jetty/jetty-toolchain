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

/**
 * The DefaultClientConfiguration is a concrete implementation of a client
 * configuration. Developers may subclass this class in order to provide their
 * own custom configuration behaviors.
 */
public class DefaultClientConfiguration implements ClientEndpointConfiguration {
    private List<String> preferredSubprotocols;
    private List<Decoder> decoders;
    private List<Encoder> encoders;
    private List<String> extensions;

    /**
     * Creates a client configuration with no preferred sub protocols,
     * extensions, decoders or encoders.
     */
    public DefaultClientConfiguration() {
	// default constructor
    }

    /**
     * Assign the list of decoders this client will use.
     * 
     * @return the decoders to use.
     */
    @Override
    public List<Decoder> getDecoders() {
	// FIXME: is this allowed to be null?
	return decoders;
    }

    /**
     * Assign the list of encoders this client will use.
     * 
     * @return the encoder list.
     */
    @Override
    public List<Encoder> getEncoders() {
	// FIXME: is this allowed to be null?
	return encoders;
    }

    /**
     * Return the extensions, in order of preference, favorite first, that this
     * client would like to use for its sessions.
     * 
     * @return the extension list.
     */
    @Override
    public List<String> getExtensions() {
	// FIXME: is this allowed to be null?
	return extensions;
    }

    /**
     * Return the protocols, in order of preference, favorite first, that this
     * client would like to use for its sessions.
     * 
     * @return the preferred subprotocols.
     */
    @Override
    public List<String> getPreferredSubprotocols() {
	// FIXME: is this allowed to be null?
	return preferredSubprotocols;
    }

    /**
     * Assign the list of decoders this client will use.
     * 
     * @param decoders
     *            the extensions
     * @return this endpoint configuration.
     */
    public ClientEndpointConfiguration setDecoders(List<Decoder> decoders) {
	// FIXME: copy of list or use as-is?
	this.decoders.clear();
	this.decoders.addAll(decoders);
	return this;
    }

    /**
     * Assign the list of encoders this client will use.
     * 
     * @param encoders
     *            the encoders to use.
     * @return this endpoint configuration.
     */
    public ClientEndpointConfiguration setEncoders(List<Encoder> encoders) {
	// FIXME: copy of list or use as-is?
	this.encoders.clear();
	this.encoders.addAll(encoders);
	return this;
    }

    /**
     * Assign the List of preferred extensions that this client would like to
     * use.
     * 
     * @param extensions
     *            the extensions
     * @return this endpoint configuration.
     */
    public ClientEndpointConfiguration setExtensions(List<String> extensions) {
	// FIXME: need language about extension order importance
	// FIXME: need language about configuration/param requirement
	// FIXME: copy of list or use as-is?
	this.extensions.clear();
	this.extensions.addAll(extensions);
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
	// FIXME: need language about order importance
	// FIXME: copy of list or use as-is?
	this.preferredSubprotocols.clear();
	this.preferredSubprotocols.addAll(preferredSubprotocols);
	return this;
    }
}
