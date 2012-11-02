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
 * This annotation may be used to annotate method parameters on web socket POJOs
 * where a URI-template has been used in the path-mapping of the
 * WebSocketEndpoint annotation.
 * <p>
 * For example:- * For example:
 * 
 * <pre>
 * &#064;WebSocketEndpoint("/bookings/{guest-id}");
 * public class BookingServer { 
 *   &#064;WebSocketMessage 
 *   public void processBookingRequest(&#064;WebSocketPathParam("guest-id") String guestID, String message, Session session) { 
 *     // process booking from the given guest here
 *   } 
 * }
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER) // FIXME (was METHOD)
public @interface WebSocketPathParam {
    /**
     * The name of the variable used in the URI-template. If the name does not
     * match a path variable in the URI-template, the value of the method
     * parameter this annotation annotates is null.
     * 
     * @return the value
     */
    public abstract String value();

}
