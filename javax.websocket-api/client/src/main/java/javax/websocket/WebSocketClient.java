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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The WebSocketClient annotation a class level annotation is used to denote
 * that a POJO is a web socket client and can be deployed as such. Similar to
 * {@link javax.websocket.server.WebSocketEndpoint WebSocketEndpoints}, POJOs
 * that are annotated with this annotation can have methods that, using the web
 * socket method level annotations, are web socket lifecycle methods.
 * <p>
 * For example:
 * 
 * <code>
 * <pre>
 * &#064;WebSocketClient(subprotocols = &quot;chat&quot;)
 * public class ChatClient {
 *     &#064;WebSocketMessage
 *     public void processMessageFromServer(String message, Session session) {
 * 	System.out.println(&quot;Message came from the server : &quot; + message);
 *     }
 * }
 * </pre>
 * </code>
 * 
 * @see DRAFT 012
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface WebSocketClient {
    /**
     * The array of Java classes that are to act as Decoders for messages coming
     * into the client.
     * 
     * @return the array of decoders.
     */
    Class<? extends Decoder>[] decoders() default {};

    /**
     * The array of Java classes that are to act as Encoders for messages sent
     * by the client.
     * 
     * @return the array of decoders.
     */
    Class<? extends Encoder>[] encoders() default {};

    /**
     * The names of the subprotocols this client supports.
     * 
     * @return the array of names of the subprotocols.
     */
    String[] subprotocols() default {}; // the subprotocols the client wants
}
