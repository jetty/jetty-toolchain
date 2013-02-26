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

import java.util.ServiceLoader;

/**
 * Provider class that allows the developer to get a reference to the
 * implementation of the WebSocketContainer. The provider class uses the <a
 * href="http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html">
 * ServiceLoader</a> to load an implementation of ContainerProvider.
 * Specifically, the fully qualified classname of the container implementation
 * of ContainerProvider must be listed in the
 * META-INF/services/javax.websocket.ContainerProvider file in the
 * implementation JAR file.
 * 
 * @see DRAFT 012
 */
public abstract class ContainerProvider {

    /**
     * Obtain a new instance of a WebSocketContainer. The method looks for the
     * ContainerProvider implementation class in the order listed in the
     * META-INF/services/javax.websocket.ContainerProvider file, returning the
     * WebSocketContainer implementation from the ContainerProvider
     * implementation that is not null.
     * 
     * @return an implementation provided instance of type WebSocketContainer
     */
    public static WebSocketContainer getWebSocketContainer() {
	WebSocketContainer wsc = null;
	for (ContainerProvider impl : ServiceLoader
		.load(ContainerProvider.class)) {
	    wsc = impl.getContainer(WebSocketContainer.class);
	    if (wsc != null) {
		return wsc;
	    }
	}

	throw new RuntimeException("Could not find an implementation class.");
    }

    /**
     * Load the container implementation.
     * 
     * @param <T>
     *            the implementation class
     * @param containerClass
     * @return the implementation class
     */
    protected abstract <T> T getContainer(Class<T> containerClass);
}
