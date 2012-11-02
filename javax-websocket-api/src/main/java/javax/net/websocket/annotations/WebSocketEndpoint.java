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

package javax.net.websocket.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.net.websocket.Decoder;
import javax.net.websocket.Encoder;

/**
 * This class level annotation declares that the class it decorates is a web
 * socket endpoint. The annotation allows the developer to define the URL (or
 * URI template) which this endpoint must be published, and other important
 * properties of the endpoint to the websocket runtime, such as the encoders it
 * uses to send messages.
 * <p>
 * The annotated class must have a public no-arg constructor.
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;WebSocketEndpoint("/hello"); 
 * public class HelloServer { 
 *   {@link WebSocketMessage &#064;WebSocketMessage} 
 *   public void processGreeting(String message, Session session) {
 *     System.out.println("Greeting received:" + message);
 *   } 
 * }
 * </pre>
 * 
 * @since Draft 002
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WebSocketEndpoint {
    /**
     * The ordered array of decoder classes this endpoint will use. For example,
     * if the developer has provided a MysteryObject decoder, this endpoint will
     * be able to receive MysteryObjects as web socket messages. The websocket
     * runtime will use the first decoder in the list able to decode a message,
     * ignoring the remaining decoders.
     */
    public Class<? extends Decoder>[] decoders() default {};

    /**
     * The ordered array of encoder classes this endpoint will use. For example,
     * if the developer has provided a MysteryObject encoder, this class will be
     * able to send web socket messages in the form of MysteryObjects. The
     * websocket runtime will use the first encoder in the list able to encode a
     * message, ignoring the remaining encoders.
     */
    public Class<? extends Encoder>[] encoders() default {};

    /**
     * The ordered array of web socket protocols this endpoint supports. For
     * example, {'superchat', 'chat'}.
     */
    public String[] subprotocols() default {};

    /**
     * The URI or URI-template (level-1) where the endpoint will be deployed.
     * The URI us relative to the root of the web socket container.
     * <p>
     * Examples:
     * 
     * <pre>
     * &#064;WebSocketEndpoint("/chat")
     * &#064;WebSocketEndpoint("/chat/{user}")
     * &#064;WebSocketEndpoint("/booking/{privilege-level}")
     * </pre>
     */
    public String value();

}
