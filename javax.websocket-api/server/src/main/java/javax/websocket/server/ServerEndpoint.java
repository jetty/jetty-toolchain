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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.websocket.Decoder;
import javax.websocket.Encoder;

/**
 * This class level annotation declares that the class it decorates is a web socket endpoint that will be deployed and made available in the URI-space of a web
 * socket server. The annotation allows the developer to define the URL (or URI template) which this endpoint will be published, and other important properties
 * of the endpoint to the websocket runtime, such as the encoders it uses to send messages. <br>
 * <br>
 * The annotated class must have a public no-arg constructor.<br>
 * <p/>
 * For example: <br>
 * <code><br>
 * <p/>
 * &nbsp;@ServerEndpoint("/hello");<br>
 * public class HelloServer {<br><br>
 * <p/>
 * &nbsp;&nbsp;@OnMessage<br>
 * &nbsp;public void processGreeting(String message, Session session) {<br>
 * &nbsp;&nbsp;&nbsp;System.out.println("Greeting received:" + message);<br>
 * &nbsp;}<br>
 * }
 * </code>
 * 
 * @see DRAFT 013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServerEndpoint
{

    /**
     * The optional custom configurator class that the developer would like to use to further configure new instances of this endpoint. If no configurator class
     * is provided, the implementation uses its own. The implementation creates a new instance of the configurator per logical endpoint.
     * 
     * @return the custom configuration class, or ServerEndpointConfigurator.class if none was set in the annotation.
     */
    public Class<? extends ServerEndpointConfigurator> configurator() default ServerEndpointConfigurator.class;

    /**
     * The ordered array of decoder classes this endpoint will use. For example, if the developer has provided a MysteryObject decoder, this endpoint will be
     * able to receive MysteryObjects as web socket messages. The websocket runtime will use the first decoder in the list able to decode a message, ignoring
     * the remaining decoders.
     * 
     * @return the decoders.
     */
    public Class<? extends Decoder>[] decoders() default {};

    /**
     * The ordered array of encoder classes this endpoint will use. For example, if the developer has provided a MysteryObject encoder, this class will be able
     * to send web socket messages in the form of MysteryObjects. The websocket runtime will use the first encoder in the list able to encode a message,
     * ignoring the remaining encoders.
     * 
     * @return the encoders.
     */
    public Class<? extends Encoder>[] encoders() default {};

    /**
     * The ordered array of web socket protocols this endpoint supports. For example, {'superchat', 'chat'}.
     * 
     * @return the subprotocols.
     */
    public String[] subprotocols() default {};

    /**
     * The URI or URI-template, level-1 (<a href="http://http://tools.ietf.org/html/rfc6570">See RFC 6570</a>) where the endpoint will be deployed. The URI us
     * relative to the root of the web socket container and must begin with a leading "/". Trailing "/"'s are ignored. Examples:<br>
     * <code>
     * &nbsp;@ServerEndpoint("/chat") <br>
     * &nbsp;@ServerEndpoint("/chat/{user}") <br>
     * &nbsp;@ServerEndpoint("/booking/{privilege-level}") <br>
     * </code>
     * 
     * @return the URI or URI-template
     */
    public String value();
}
