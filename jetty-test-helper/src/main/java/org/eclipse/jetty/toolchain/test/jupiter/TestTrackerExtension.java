//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.toolchain.test.jupiter;

import java.lang.reflect.Method;

import org.eclipse.jetty.toolchain.test.StringMangler;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class TestTrackerExtension implements BeforeEachCallback
{
    @Override
    public void beforeEach(ExtensionContext extensionContext)
    {
        Boolean logDisplay = Boolean.getBoolean("jetty.testtracker.log");
        if (logDisplay)
        {
            Method method = extensionContext.getRequiredTestMethod();
            Class<?> clazz = extensionContext.getRequiredTestClass();
            // until junit issue fixed https://github.com/junit-team/junit5/issues/1139
            // we cannot get argument values so use a mix with displayName for methods with args
            if (method.getParameterCount() > 0)
            {
                String displayName = extensionContext.getDisplayName();
                if (displayName.contains(method.getName()))
                {
                    // this display name contains the method.
                    System.err.printf("Running %s.%s%n",
                        clazz.getName(),
                        StringMangler.escapeJava(displayName));
                }
                else
                {
                    // this display name does not contain method name, so include it.
                    System.err.printf("Running %s.%s(%s)%n",
                        clazz.getName(),
                        method.getName(),
                        StringMangler.escapeJava(displayName));
                }
            }
            else
            {
                // this one has no parameters
                System.err.printf("Running %s.%s()%n",
                    clazz.getName(),
                    method.getName());
            }
        }
    }
}
