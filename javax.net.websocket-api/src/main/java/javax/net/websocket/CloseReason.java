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

package javax.net.websocket;

/**
 * A class encapsulating the reason why a web socket has been closed, or why it
 * is being asked to close. Note the acceptable uses of codes and reason phrase
 * defined in RFC 6455.
 * 
 * @since DRAFT 001
 */
public class CloseReason extends java.lang.Object {
    /**
     * A marker interface for the close codes. This interface may be implemented
     * by enumerations that contain web socket close codes, for example
     * enumerations that contain all the in use close codes as of web socket
     * 1.0, or an enumeration that contains close codes that are currently
     * reserved for special use by the web socket specification.
     */
    public static interface CloseCode {
	/**
	 * Returns the code number, for example the integer '1000' for normal
	 * closure.
	 * 
	 * @return the code number
	 */
	int getCode();
    }

    /**
     * An Enumeration of status codes for a web socket close that are defined in
     * the specification.
     */
    public static enum CloseCodes implements CloseReason.CloseCode {
	/**
	 * 1000 indicates a normal closure, meaning that the purpose for which
	 * the connection was established has been fulfilled.
	 */
	NORMAL_CLOSURE(1000),
	/**
	 * 1001 indicates that an endpoint is "going away", such as a server
	 * going down or a browser having navigated away from a page.
	 */
	GOING_AWAY(1001),
	/**
	 * 1002 indicates that an endpoint is terminating the connection due to
	 * a protocol error.
	 */
	PROTOCOL_ERROR(1002),
	/**
	 * 1003 indicates that an endpoint is terminating the connection because
	 * it has received a type of data it cannot accept (e.g., an endpoint
	 * that understands only text data MAY send this if it receives a binary
	 * message).
	 */
	CANNOT_ACCEPT(1003),
	/** 1004 Reserved. The specific meaning might be defined in the future. */
	RESERVED(1004),
	/**
	 * 1005 is a reserved value and MUST NOT be set as a status code in a
	 * Close control frame by an endpoint. It is designated for use in
	 * applications expecting a status code to indicate that no status code
	 * was actually present.
	 */
	NO_STATUS_CODE(1005),
	/**
	 * 1006 is a reserved value and MUST NOT be set as a status code in a
	 * Close control frame by an endpoint. It is designated for use in
	 * applications expecting a status code to indicate that the connection
	 * was closed abnormally, e.g., without sending or receiving a Close
	 * control frame.
	 */
	CLOSED_ABNORMALLY(1006),
	/**
	 * 1007 indicates that an endpoint is terminating the connection because
	 * it has received data within a message that was not consistent with
	 * the type of the message (e.g., non-UTF-8 [RFC3629] data within a text
	 * message).
	 */
	NOT_CONSISTENT(1007),
	/**
	 * 1008 indicates that an endpoint is terminating the connection because
	 * it has received a message that violates its policy. This is a generic
	 * status code that can be returned when there is no other more suitable
	 * status code (e.g., 1003 or 1009) or if there is a need to hide
	 * specific details about the policy.
	 */
	VIOLATED_POLICY(1008),
	/**
	 * 1009 indicates that an endpoint is terminating the connection because
	 * it has received a message that is too big for it to process.
	 */
	TOO_BIG(1009),
	/**
	 * 1010 indicates that an endpoint (client) is terminating the
	 * connection because it has expected the server to negotiate one or
	 * more extension, but the server didn't return them in the response
	 * message of the WebSocket handshake. The list of extensions that are
	 * needed SHOULD appear in the /reason/ part of the Close frame. Note
	 * that this status code is not used by the server, because it can fail
	 * the WebSocket handshake instead.
	 */
	NO_EXTENSION(1010),
	/**
	 * 1011 indicates that a server is terminating the connection because it
	 * encountered an unexpected condition that prevented it from fulfilling
	 * the request.
	 */
	UNEXPECTED_CONDITION(1011),
	// FIXME: missing codes from
	// https://www.ietf.org/mail-archive/web/hybi/current/msg09649.html
	// FIXME: missing 1012 (SERVICE_RESTART)
	// FIXME: missing 1013 (TRY_AGAIN_LATER)
	/**
	 * 1015 is a reserved value and MUST NOT be set as a status code in a
	 * Close control frame by an endpoint. It is designated for use in
	 * applications expecting a status code to indicate that the connection
	 * was closed due to a failure to perform a TLS handshake (e.g., the
	 * server certificate can't be verified).
	 */
	TLS_HANDSHAKE_FAILURE(1015);

	private int code;

	private CloseCodes(int value) {
	    this.code = value;
	}

	/**
	 * Return the code number of this status code.
	 */
	@Override
	public int getCode() {
	    return this.code;
	}
    }

    private CloseReason.CloseCode closeCode;
    private String reasonPhrase;

    /**
     * Creates a reason for closing a web socket connection with the given code
     * and reason phrase.
     * 
     * @param closeCode
     * @param reasonPhrase
     */
    public CloseReason(CloseReason.CloseCode closeCode, String reasonPhrase) {
	this.closeCode = closeCode;
	this.reasonPhrase = reasonPhrase;
    }

    /**
     * The Close code associated with this CloseReason
     * 
     * @return the close code
     */
    public CloseReason.CloseCode getCloseCode() {
	return this.closeCode;
    }

    /**
     * The reason phrase associated with this CloseReason.
     * 
     * @return the reason phrase.
     */
    public String getReasonPhrase() {
	return this.reasonPhrase;
    }
}
