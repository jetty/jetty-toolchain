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

/**
 * This annotation may be used to annotate method parameters on server endpoints where a URI-template has been used in the path-mapping of the
 * {@link ServerEndpoint} annotation. The method parameter may be of type String, any Java primitive type or any boxed version thereof. If a client URI matches
 * the URI-template, but the requested path parameter cannot be decoded, then the websocket's error handler will be called.
 * <p/>
 * <p/>
 * <br>
 * For example:- <br>
 * <code><br>
 * <p/>
 * &nbsp;@ServerEndpoint("/bookings/{guest-id}");<br>
 * public class BookingServer {<br><br>
 * <p/>
 * &nbsp;&nbsp;@OnMessage<br>
 * &nbsp;public void processBookingRequest(@PathParam("guest-id") String guestID, String message, Session session) {<br>
 * &nbsp;&nbsp;&nbsp;// process booking from the given guest here<br>
 * &nbsp;}<br>
 * }
 * </code>
 * <p/>
 * <br>
 * For example:- <br>
 * <code><br>
 * <p/>
 * &nbsp;@ServerEndpoint("/rewards/{vip-level}");<br>
 * public class RewardServer {<br><br>
 * <p/>
 * &nbsp;&nbsp;@OnMessage<br>
 * &nbsp;public void processReward(@PathParam("vip-level") Integer vipLevel, String message, Session session) {<br>
 * &nbsp;&nbsp;&nbsp;// process reward here<br>
 * &nbsp;}<br>
 * }
 * </code>
 * 
 * @see DRAFT 013
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathParam
{
    /**
     * The name of the variable used in the URI-template. If the name does not match a path variable in the URI-template, the value of the method parameter this
     * annotation annotates is null.
     * 
     * @return the name of the variable used in the URI-template.
     */
    public String value();
}
