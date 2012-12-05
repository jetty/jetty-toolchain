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

/**
 * A general exception that occurs when trying to encode a custom object to a
 * string or binary message.
 * 
 * @since DRAFT 002
 */
public class EncodeException extends Exception {
    private Object object;

    /**
     * Constructor with the object being encoded, and the reason why it failed
     * to be.
     * 
     * @param object
     *            the object that could not be encoded.
     * @param message
     *            the reason for the failure.
     */
    public EncodeException(Object object, String message) {
	super(message);
	this.object = object;
    }

    /**
     * Constructor with the object being encoded, and the reason why it failed
     * to be, and the cause.
     * 
     * @param object
     *            the object that could not be encoded.
     * @param message
     *            the reason for the failure.
     * @param cause
     *            the cause of the problem
     */
    public EncodeException(Object object, String message, Throwable cause) {
	super(message, cause);
	this.object = object;
    }

    /**
     * Return the Object that could not be encoded.
     * 
     * @return the object
     */
    public Object getObject() {
	return object;
    }
}
