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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation may be used to annotate method parameters on server side web
 * socket POJOs where a URI-template has been used in the path-mapping of the
 * WebSocketEndpoint annotation. The method parameter may be of type String or
 * any Java primitive type or any boxed version thereof. If a client URI matches
 * the URI-template, but the requested path parameter cannot be decoded, then
 * the websocket's error handler will be called.
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;WebSocketEndpoint(&quot;/bookings/{guest-id}&quot;)
 * public class BookingServer {
 *     &#064;WebSocketMessage
 *     public void processBookingRequest(
 * 	    &#064;WebSocketPathParam(&quot;guest-id&quot;) String guestID, String message,
 * 	    Session session) {
 * 	// process booking from the given guest here
 *     }
 * }
 * </pre>
 * <p>
 * For example:
 * 
 * <pre>
 * &#064;WebSocketEndpoint(&quot;/rewards/{vip-level}&quot;)
 * public class RewardServer {
 *     &#064;WebSocketMessage
 *     public void processReward(
 * 	    &#064;WebSocketPathParam(&quot;vip-level&quot;) Integer vipLevel, String message,
 * 	    Session session) {
 * 	// process reward here
 *     }
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface WebSocketPathParam {
    /**
     * The name of the variable used in the URI-template. If the name does not
     * match a path variable in the URI-template, the value of the method
     * parameter this annotation annotates is null.
     * 
     * @return the name of the variable used in the URI-template.
     */
    public abstract String value();

}
