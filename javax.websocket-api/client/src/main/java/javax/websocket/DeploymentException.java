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
 * http://java.net/jira/browse/WEBSOCKET_SPEC-47
 * <p>
 * Checked exception indicating some kind of failure either to publish an
 * endpoint on its server, or a failure to connect a client to its server.
 * 
 * @see DRAFT 013
 */
public class DeploymentException extends Exception {
    private static final long serialVersionUID = 7576860738144220015L;

    /**
     * Creates a deployment exception with the given reason for the deployment
     * failure.
     * 
     * @param message
     *            the reason for the failure.
     */
    public DeploymentException(String message) {
	super(message);
    }

    /**
     * Creates a deployment exception with the given reason for the deployment
     * failure and wrapped cause of the failure.
     * 
     * @param message
     *            the reason for the failure.
     * @param cause
     *            the cause of the problem.
     */
    public DeploymentException(String message, Throwable cause) {
	super(message, cause);
    }
}
