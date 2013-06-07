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

package org.eclipse.jetty.toolchain.test;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * Adds simple System.err output tracking of test method execution.
 * <p>
 * Note: {@link AdvancedRunner} performs tracking as well, there is no need
 * for this &#064;Rule if you have AdvancedRunner in use.
 * <p>
 * <pre>
 *   &#064;Rule
 *   public TestTracker ttracker = new TestTracker();
 * </pre>
 */
public class TestTracker extends TestWatcher
{
    @Override
    protected void starting(Description description)
    {
        super.starting(description);
        System.err.printf("Running %s.%s()%n",
                description.getClassName(),
                description.getMethodName());
    }
}
