//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
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

/**
 * The ClientEndpointConfigurationBuilder is a class used for creating
 * {@link ClientEndpointConfiguration} objects for the purposes of deploying a
 * client endpoint. <br>
 * <br>
 * Here are some examples:<br>
 * <br>
 * Building a plain configuration with no encoders, decoders, subprotocols or
 * extensions.<br>
 * <br>
 * <code>
 * ClientEndpointConfiguration cec = ClientEndpointConfigurationBuilder.create().build();<br>
 * </code>
 * 
 * <br>
 * <br>
 * Building a configuration with no subprotocols and a custom configurator.<br>
 * <br>
 * <code>
 * ClientEndpointConfiguration customCec = ClientEndpointConfigurationBuilder.create()<br>
 *  &nbsp;&nbsp;             .preferredSubprotocols(mySubprotocols)<br>
 *  &nbsp;&nbsp;             .clientHandshakeConfigurator(new MyClientConfigurator())<br>
 *  &nbsp;&nbsp;             .build();<br>
 * </code>
 * 
 * @see DRAFT 013
 */
public class ClientEndpointConfigurationBuilder {
    /**
     * Creates a new builder object with no subprotocols, extensions, encoders,
     * decoders and a null configurator.
     * 
     * @return a new builder object.
     */
    public static ClientEndpointConfigurationBuilder create() {
	return new ClientEndpointConfigurationBuilder();
    }

    private List<String> preferredSubprotocols = new ArrayList<String>();
    private List<Extension> extensions = new ArrayList<Extension>();
    private List<Encoder> encoders = new ArrayList<Encoder>();
    private List<Decoder> decoders = new ArrayList<Decoder>();

    private ClientEndpointConfigurator clientEndpointConfigurator = new ClientEndpointConfigurator() {

    };

    /**
     * Builds a configuration object using the attributes set on this builder.
     * 
     * @return a new configuration object.
     */
    public ClientEndpointConfiguration build() {
	return new DefaultClientEndpointConfiguration(
		Collections.unmodifiableList(this.preferredSubprotocols),
		Collections.unmodifiableList(this.extensions),
		Collections.unmodifiableList(this.encoders),
		Collections.unmodifiableList(this.decoders),
		this.clientEndpointConfigurator);
    }

    /**
     * Sets the configurator object for the configuration this builder will
     * build.
     * 
     * @param clientEndpointConfigurator
     *            the configurator
     * @return the builder instance
     */
    public ClientEndpointConfigurationBuilder clientHandshakeConfigurator(
	    ClientEndpointConfigurator clientEndpointConfigurator) {
	this.clientEndpointConfigurator = clientEndpointConfigurator;
	return this;
    }

    /**
     * Assign the list of decoders the client will use.
     * 
     * @param decoders
     *            the decoders
     * @return this builder instance
     */
    public ClientEndpointConfigurationBuilder decoders(List<Decoder> decoders) {
	this.decoders = (decoders == null) ? new ArrayList<Decoder>()
		: decoders;
	return this;
    }

    /**
     * Assign the list of encoders the client will use.
     * 
     * @param encoders
     *            the encoders
     * @return the builder instance
     */
    public ClientEndpointConfigurationBuilder encoders(List<Encoder> encoders) {
	this.encoders = (encoders == null) ? new ArrayList<Encoder>()
		: encoders;
	return this;
    }

    /**
     * Set the extensions for the configuration this builder will build. The
     * list is treated in order of preference, favorite first, that the client
     * would like to use for its sessions.
     * 
     * @param extensions
     *            the extensions
     * @return the builder instance
     */
    public ClientEndpointConfigurationBuilder extensions(
	    List<Extension> extensions) {
	this.extensions = (extensions == null) ? new ArrayList<Extension>()
		: extensions;
	return this;
    }

    /**
     * Set the preferred sub protocols for the configuration this builder will
     * build. The list is treated in order of preference, favorite first, that
     * this client would like to use for its sessions.
     * 
     * @param preferredSubprotocols
     *            the preferred subprotocol names.
     * @return the builder instance
     */
    public ClientEndpointConfigurationBuilder preferredSubprotocols(
	    List<String> preferredSubprotocols) {
	this.preferredSubprotocols = (preferredSubprotocols == null) ? new ArrayList<String>()
		: preferredSubprotocols;
	return this;
    }

}
