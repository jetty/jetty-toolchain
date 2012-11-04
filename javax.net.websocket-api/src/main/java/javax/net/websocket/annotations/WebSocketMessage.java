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

/**
 * This method level annotation can be used to make a Java method receive
 * incoming web socket messages. It must have parameters of the following types
 * otherwise the container will generate an error at deployment time
 * <p>
 * String / byte[] / or decodable (as determined by the Decoders configured for
 * the endpoint) parameter
 * <p>
 * Optional Session parameter
 * <p>
 * Zero to n String parameters annotated with the @WebSocketPathParam
 * annotation.
 * <p>
 * The parameters may be listed in any order.
 * <p>
 * The method may have a non-void return type, in which case the web socket
 * runtime must interpret this as a web socket message to return to the peer.
 * The allowed data types for this return type, other than void, are String,
 * ByteBuffer, byte[], any java primitive or class equivalent, and array or
 * Collection of any of the previous types, plus anything for which there is a
 * decoder.
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;WebSocketMessage; 
 * public void processGreeting(String message, Session session) {
 *   System.out.println("Greeting received:" + message); 
 * }
 * @since Draft 002
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebSocketMessage {
    /**
     * Specifies the maximum size of message in bytes that the method this
     * annotates will be able to process, or -1 to indicate that there is no
     * maximum. The default is -1.
     * 
     * @return the maximum size in bytes
     */
    public long maxMessageSize() default -1L;

}
