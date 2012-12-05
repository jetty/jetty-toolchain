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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * The DefaultServerConfiguration is a concrete class that embodies all the
 * configuration parameters for an endpoint that is to be published as a server
 * endpoint. Developers may subclass this class in order to override the
 * configuration behavior.
 */
public class DefaultServerConfiguration implements ServerEndpointConfiguration {
    private String path;
    private List<Decoder> decoders;
    private List<Encoder> encoders;
    private List<String> extensions;
    private List<String> subprotocols;

    /**
     * For subclass implementations.
     */
    protected DefaultServerConfiguration() {
	/* do nothing */
    }

    /**
     * Creates a server configuration with the given path
     * 
     * @param path
     *            the URI or URI template.
     */
    public DefaultServerConfiguration(String path) {
	this.path = path;
    }

    /**
     * Makes a check of the validity of the Origin header sent along with the
     * opening handshake following the recommendation at:
     * http://tools.ietf.org/html/rfc6455#section-4.2
     * 
     * @return whether the check passed or not.
     */
    @Override
    public boolean checkOrigin(String originHeaderValue) {
	// FIXME: need language that this is for subclasses?
	// FIXME: where does opening handshake information come from?
	return true;
    }

    /**
     * Return the Decoder implementations configured. These will be used by the
     * container to decode incoming messages into the expected custom objects on
     * MessageHandler callbacks.
     * 
     * @return the decoders.
     */
    @Override
    public List<Decoder> getDecoders() {
	return decoders;
    }

    /**
     * Return the Encoder implementations configured. These will be used by the
     * container to encode outgoing messages.
     * 
     * @return the encoders.
     */
    @Override
    public List<Encoder> getEncoders() {
	return encoders;
    }

    /**
     * Provides a simple algorithm to return the list of extensions this server
     * will use for the web socket session: the configuration returns a list
     * containing all of the requested extensions passed to this method that it
     * supports, using the order in the requested extensions, the empty list if
     * none. Subclasses may provide custom algorithms based on other factors.
     * 
     * @return the list of extensions.
     */
    @Override
    public List<String> getNegotiatedExtensions(List<String> requestedExtensions) {
	List<String> negotiated = new ArrayList<String>();
	for (String requestedExtension : requestedExtensions) {
	    for (String availableExtension : extensions) {
		if (availableExtension.equals(requestedExtension)) {
		    // found a match, by-name
		    // FIXME: match by parameters?
		    // FIXME: match by acceptable configuration?
		    // FIXME: match by supported configuration?
		    negotiated.add(requestedExtension);
		}
	    }
	}
	return negotiated;
    }

    /**
     * The default implementation of this method returns, the first subprotocol
     * in the list sent by the client that the server supports, or null if there
     * isn't one none. Subclasses may provide custom algorithms based on other
     * factors.
     * <p>
     * See <a href="http://tools.ietf.org/html/rfc6455#section-4.2.2"> Sending
     * the Server's Opening Handshake</a>
     * 
     * @param requestedSubprotocols
     * @return the negotiated subprotocol
     */
    @Override
    public String getNegotiatedSubprotocol(List<String> requestedSubprotocols) {
	for (String requestedSubprotocol : requestedSubprotocols) {
	    if (subprotocols.contains(requestedSubprotocol)) {
		return requestedSubprotocol;
	    }
	}
	return null;
    }

    /**
     * Return the path of this server configuration. The path is a relative URI
     * or URI-template.
     * 
     * @return the path
     */
    @Override
    public String getPath() {
	return path;
    }

    /**
     * This default implementation matches the incoming path to the
     * configuration's URI or URI template if and only if it is an exact match
     * in the case the configuration is a URI, and if and only if it is a valid
     * expansion of the configuration URI template, in the case where the
     * configuration is a URI template. Subclasses may override this method to
     * provide different matching policies.
     * 
     * @param uri
     *            the URL of the incoming request
     * @return whether it matched this configuration or not.
     */
    @Override
    public boolean matchesURI(URI uri) {
	// FIXME: need language about what to do when input URI is null
	return this.path.equals(uri.toString());
    }

    /**
     * The default server configuration does not make any changes to the
     * response. Subclasses may override this method in order to inspect the
     * Http request headers of the openinghandshake, for example to track
     * cookies sent by the client. Additionally subclasses may choose to
     * override this method to modify the outgoing handshake response. the
     * outgoing handshake response
     * 
     * @param request
     *            the handshake request from the client
     * @param response
     *            the handshake response formulated by the container.
     */
    @Override
    public void modifyHandshake(HandshakeRequest request,
	    HandshakeResponse response) {
	// do nothing
    }

    /**
     * Sets all the decoders that this configuration will support.
     * 
     * @param decoders
     *            the encoders supported
     * @return this server configuration instance.
     */
    public DefaultServerConfiguration setDecoders(List<Decoder> decoders) {
	// FIXME: is this a copy of the list, or use as-is?
	this.decoders.clear();
	this.decoders.addAll(decoders);
	return this;
    }

    /**
     * Sets all the encoders that this configuration will support.
     * 
     * @param encoders
     *            the encoders supported
     * @return this server configuration instance.
     */
    public DefaultServerConfiguration setEncoders(List<Encoder> encoders) {
	// FIXME: is this a copy of the list, or use as-is?
	this.encoders.clear();
	this.encoders.addAll(encoders);
	return this;
    }

    /**
     * Sets all the extensions that this configuration will support.
     * 
     * @param extensions
     *            the encoders supported
     * @return this server configuration instance.
     */
    public DefaultServerConfiguration setExtensions(List<String> extensions) {
	this.extensions.clear();
	this.extensions.addAll(extensions);
	return this;
    }

    /**
     * Sets all the subprotocols that this configuration will support.
     * 
     * @param subprotocols
     *            the encoders supported
     * @return this server configuration instance.
     */
    public DefaultServerConfiguration setSubprotocols(List<String> subprotocols) {
	// FIXME: need language about preference and order of protocols
	this.subprotocols.clear();
	this.subprotocols.addAll(subprotocols);
	return this;
    }
}
