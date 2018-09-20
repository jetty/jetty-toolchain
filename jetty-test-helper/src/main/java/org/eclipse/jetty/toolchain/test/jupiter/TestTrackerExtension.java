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

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

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
                                   safe(displayName) );
            }
            else
            {
                System.err.printf( "Running %s.%s()%n", //
                                   clazz.getName(), //
                                   method.getName() );
            }
        }
    }

    /**
     * Clean the input string of control characters that can
     * impact the output to the logs in harmful ways.
     *
     * @param string input string
     * @return clean form of input string
     */
    public CharSequence safe(String string)
    {
        StringBuilder ret = new StringBuilder(string.length());
        for(char c: string.toCharArray())
        {
            if( (c<=0x1F) || (c==0x7F) )
            {
                switch(c)
                {
                    case '\r':
                        ret.append("\\r");
                        break;
                    case '\n':
                        ret.append("\\n");
                        break;
                    case '\t':
                        ret.append("\\t");
                        break;
                    default:
                        ret.append("\\u00").append(String.format("%02x",(byte)c));
                        break;
                }
            }
            else
            {
                ret.append(c);
            }
        }
        return ret;
    }
}
