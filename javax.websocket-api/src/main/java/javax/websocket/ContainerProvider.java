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

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Provider class that uses the ServiceLoader mechanism to provide
 * implementations of ServerContainer and ClientContainer.
 */
public class ContainerProvider {
    private static ServiceLoader<ClientContainer> clientLoader = ServiceLoader
	    .load(ClientContainer.class);
    private static ServiceLoader<ServerContainer> serverLoader = ServiceLoader
	    .load(ServerContainer.class);

    public static ClientContainer getClientContainer() {
	Iterator<ClientContainer> iter = clientLoader.iterator();
	if (iter.hasNext()) {
	    return iter.next();
	}
	System.err.printf("ERROR: ServiceLoader cannot find %s instances.%n",
		ClientContainer.class);
	return null;
    }

    public static ServerContainer getServerContainer() {
	Iterator<ServerContainer> iter = serverLoader.iterator();
	if (iter.hasNext()) {
	    return iter.next();
	}
	System.err.printf("ERROR: ServiceLoader cannot find %s instances.%n",
		ServerContainer.class);
	return null;
    }

    public ContainerProvider() {
	/* nothing to do */
    }
}
