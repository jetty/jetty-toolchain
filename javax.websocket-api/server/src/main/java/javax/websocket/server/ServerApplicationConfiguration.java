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

package javax.websocket.server;

import java.util.Set;

/**
 * Developers may include implementations of ServerApplicationConfiguration in an archive containing
 * websocket endpoints (WAR file, or JAR file within the WAR file) in order to specify precisely
 * which of the websocket endpoints within the archive the implementation must deploy. There is a separate
 * method for programmatic endpoints and for annotated endpoints.
 */
public interface ServerApplicationConfiguration
{
    /**
     * Return a set of ServerEndpointConfiguration classes that the server container
     * must deploy. The set of ServerEndpointConfiguration passed in to this method is
     * the set obtained by scanning the archive containing the implementation
     * of this interface. Therefore, this set passed in contains all the ServerEndpointConfiguration classes
     * in the JAR or WAR file containing the implementation of this interface. This set passed in
     * may be used the build the set to return to the container for deployment.
     *
     * @param scanned the set of all the ServerEndpointConfiguration classes in the archive containing
     *                the implementation of this interface.
     * @return the set of ServerEndpointConfiguration to deploy on the server.
     */
    Set<Class<? extends ServerEndpointConfiguration>> getEndpointConfigurationClasses(Set<Class<? extends ServerEndpointConfiguration>> scanned);

    /**
     * Return a set of annotated endpoint classes that the server container
     * must deploy. The set of classes passed in to this method is
     * the set obtained by scanning the archive containing the implementation
     * of this interface. Therefore, this set passed in contains all the annotated endpoint classes
     * in the JAR or WAR file containing the implementation of this interface. This set passed in
     * may be used the build the set to return to the container for deployment.
     *
     * @param scanned the set of all the annotated endpoint classes in the archive containing
     *                the implementation of this interface.
     * @return the set of annotated endpoint classes to deploy on the server.
     */
    Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned);
}
