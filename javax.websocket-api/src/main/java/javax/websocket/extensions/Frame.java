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

import javax.websocket.CloseReason;

/**
 * Frame is the top level interface that represents some kind of web socket
 * frame.
 * 
 * @since DRAFT 003
 */
public interface Frame {
    /**
     * Super type for all the websocket control frames.
     */
    public static interface Control extends Frame {
	/**
	 * A web socket Close frame.
	 */
	public static interface Close extends Frame.Control {
	    /**
	     * The close code for this close.
	     */
	    abstract CloseReason.CloseCode getCloseCode();

	    /**
	     * The reason phrase for this close.
	     */
	    abstract String getReasonPhrase();
	}

	/**
	 * A web socket Ping frame.
	 */
	public static interface Ping extends Frame.Control {
	    /**
	     * The application data within the Ping frame.
	     */
	    abstract byte[] getApplicationData();
	}

	/**
	 * A web socket Pong frame.
	 */
	public static interface Pong extends Frame.Control {
	    /**
	     * The application data within the Pong frame.
	     */
	    abstract byte[] getApplicationData();
	}
    }

    /**
     * Common super-type for all the web socket frames that carry application
     * data.
     */
    public static interface Data extends Frame {
	/**
	 * A binary data frame
	 */
	public static interface Binary extends Frame.Data {
	    /**
	     * A kind of binary frame that represents a fragment of a message in
	     * a series of such frames that, re-assembled, form a complete text
	     * message.
	     */
	    public static interface Continuation extends Frame.Data.Binary {
		/**
		 * Indicates whether this text message fragment is the last in
		 * the series or not.
		 */
		abstract boolean isLast();
	    }

	    /**
	     * The application data in the binary frame.
	     */
	    abstract byte[] getData();
	}

	/**
	 * A text data frame.
	 */
	public static interface Text extends Frame.Data {
	    /**
	     * A kind of text frame that represents a fragment of a message in a
	     * series of such frames that, re-assembled, form a complete text
	     * message.
	     */
	    public static interface Continuation extends Frame.Data.Text {
		/**
		 * Indicates whether this text message fragment is the last in
		 * the series or not.
		 */
		abstract boolean isLast();
	    }

	    /**
	     * Return the textual data in this text frame.
	     */
	    abstract String getText();
	}

	/**
	 * Return data used by a web socket extension in this frame.
	 */
	abstract byte[] getExtensionData();
    }
}
