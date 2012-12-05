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

import java.nio.ByteBuffer;

/**
 * The PingMessage interface represents a web socket ping. PingMessages may be
 * received by using a MessageHandler.Basic&lt;PingMessage>. The payload of the
 * PingMessage is the application data sent by the peer.
 * 
 * @since v008
 */
public interface PingMessage {
    /**
     * The application data inside the ping message from the peer.
     * 
     * @return the application data.
     */
    public ByteBuffer getApplicationData();
}
