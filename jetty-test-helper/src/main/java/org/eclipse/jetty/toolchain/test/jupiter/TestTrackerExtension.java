//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.toolchain.test.jupiter;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.Optional;

public class TestTrackerExtension implements BeforeEachCallback
{

    @Override
    public void beforeEach( ExtensionContext extensionContext )
    {
        //String displayName = extensionContext.getDisplayName();
        // we do not manage displayName here
        Optional<Method> method = extensionContext.getTestMethod();
        Optional<Class<?>> clazz = extensionContext.getTestClass();
        System.err.printf("Running %s.%s()%n",
                          clazz.isPresent()?clazz.get().getName():"",
                          method.isPresent()?method.get().getName():"");
    }
}
