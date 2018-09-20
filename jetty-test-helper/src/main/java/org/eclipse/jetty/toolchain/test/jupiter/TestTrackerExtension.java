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

public class TestTrackerExtension implements BeforeEachCallback
{

    @Override
    public void beforeEach( ExtensionContext extensionContext )
    {
        Boolean logDisplay = Boolean.getBoolean("jetty.testtracker.log");
        if(logDisplay)
        {
            Method method = extensionContext.getRequiredTestMethod();
            Class<?> clazz = extensionContext.getRequiredTestClass();
            // until junit issue fixed https://github.com/junit-team/junit5/issues/1139
            // we cannot get argument values so use a mix with displayName for methods with args
            if ( method.getParameterCount() > 0 )
            {
                String displayName = extensionContext.getDisplayName();
                System.err.printf( "Running %s.%s(%s)%n", //
                                   clazz.getName(), //
                                   method.getName(), //
                                   displayName );
            }
            else
            {
                System.err.printf( "Running %s.%s()%n", //
                                   clazz.getName(), //
                                   method.getName() );
            }
        }
    }
}
