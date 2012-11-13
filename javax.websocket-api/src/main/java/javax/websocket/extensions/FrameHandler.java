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

package javax.websocket.extensions;

/**
 * A FrameHandler is a link in the chain of handlers associated with the web
 * socket extensions configured for an endpoint. Each framehandler belongs to an
 * extension, either as the handler for all the incoming web socket frames, of
 * as the handler for all the outgoing web socket frames. on per connection.
 * 
 * @since DRAFT 003
 */
public abstract class FrameHandler extends java.lang.Object {
    private final FrameHandler nextHandler;

    /**
     * Constructor that creates a FrameHandler with the given framehandler as
     * the next frame handler in the chain.
     * 
     * @param nextHandler
     */
    public FrameHandler(FrameHandler nextHandler) {
	this.nextHandler = nextHandler;
    }

    /**
     * The next handler in the handler chain.
     */
    public FrameHandler getNextHandler() {
	return nextHandler;
    }

    /**
     * This method is invoked whenever the implementation is ready to invoke
     * this framehandler as part of the framehandler chain. The defauly
     * implementation in this class is a no-op: i.e. it simply invokes the next
     * handler in the chain with the frame passed in.
     */
    public void handleFrame(Frame f) {
	nextHandler.handleFrame(f);
    }
}
