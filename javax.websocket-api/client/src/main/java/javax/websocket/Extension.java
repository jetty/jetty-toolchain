//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
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

import java.util.List;

/**
 * A simple representation of a websocket extension as a name and map of
 * extension parameters.
 * 
 * @see DRAFT 012
 */
public interface Extension {
    /**
     * This member interface defines a single websocket extension parameter.
     */
    interface Parameter {
	/**
	 * Return the name of the extension parameter.
	 * 
	 * @return the name of the parameter.
	 */
	String getName();

	/**
	 * Return the value of the extension parameter.
	 * 
	 * @return the value of the parameter.
	 */
	String getValue();
    }

    /**
     * The name of the extension.
     * 
     * @return the name of the extension.
     */
    String getName();

    /**
     * The extension parameters for this extension in the order they appear in
     * the http headers.
     * 
     * @return The read-only Map of extension parameters belonging to this
     *         extension.
     */
    List<Parameter> getParameters();
}
