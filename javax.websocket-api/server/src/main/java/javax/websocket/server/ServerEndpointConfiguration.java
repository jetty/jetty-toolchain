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

import java.net.URI;
import java.util.List;

import javax.websocket.EndpointConfiguration;
import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;

/**
 * The ServerEndpointConfiguration is a special kind of endpoint configuration object that contains
 * web socket configuration information specific only to server endpoints. The parametrized type T is
 * the type of the Endpoint that this configures.
 * 
 * @since DRAFT 001
 * @see DRAFT 012
 */
public interface ServerEndpointConfiguration extends EndpointConfiguration {

    /**
     * Returns the Class of the endpoint this configuration is configuring. If 
     * the endpoint is an annotated endpoint, the value is the class of the Java class
     * annotated with @WebSocketEndpoint. if the endpoint is a programmatic, the value
     * is the class of the subclass of Endpoint.
     *
     * @return the class of the endpoint, annotated or programmatic.
     */
    Class<?> getEndpointClass();

    /**
     * Return the subprotocol this server endpoint has chosen from the requested
     * list supplied by a client who wishes to connect, or none if there wasn't one
     * this server endpoint liked. See <a href="http://tools.ietf.org/html/rfc6455#section-4.2.2">Sending the Server's Opening Handshake</a>
     *
     * @param requestedSubprotocols the requested subprotocols.
     * @return the negotiated subprotocol.
     */
    String getNegotiatedSubprotocol(List<String> requestedSubprotocols);

    /**
     * Return the ordered list of extensions that this server will support given the requested
     * extension list passed in, the empty list if none. See <a href="http://tools.ietf.org/html/rfc6455#section-9.1">Negotiating Extensions</a>
     *
     * @param requestedExtensions the requested extentions, in order.
     * @return the list of extensions negotiated
     */
    List<Extension> getNegotiatedExtensions(List<Extension> requestedExtensions);

    /**
     * Check the value of the Origin header (<a href="http://tools.ietf.org/html/rfc6454">See Origin Header</a>) the client passed during the opening
     * handshake.
     *
     * @param originHeaderValue the value of the origin header.
     * @return whether the check passed or not
     */
    boolean checkOrigin(String originHeaderValue);

    /**
     * Answers whether the current configuration matches the given path. This method may be overridden
     * by implementations with any number of algorithms for determining a match.
     *
     * @param uri the uri of the incoming handshake.
     * @return whether there was a match
     */
    boolean matchesURI(URI uri);


    /**
     * Called by the container after it has formulated a handshake response resulting from
     * a well-formed handshake request. The container has already has already checked that this configuration
     * has a matching URI, determined the validity of the origin using the checkOrigin method, and filled
     * out the negotiated subprotocols and extensions based on this configuration.
     * Custom configurations may override this method in order to inspect
     * the request parameters and modify the handshake response that the server has formulated.
     * and the URI checking also.
     *
     * @param request  the opening handshake request.
     * @param response the proposed opening handshake response
     */
    void modifyHandshake(HandshakeRequest request, HandshakeResponse response);

    /**
     * Return the path for this endpoint configuration. The path
     * is the URI or URI-template relative to the websocket root of the server to which the endpoint
     * using this configuration will be mapped. The path always begins with a leading "/". A trailing "/" will be
     * ignored.
     *
     * @return the relative path for this configuration.
     */
    String getPath();
}
