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

import org.eclipse.jetty.toolchain.test.annotation.Slow;
import org.eclipse.jetty.toolchain.test.annotation.Stress;
import org.junit.Ignore;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * Tiered Junit 4 Test Runner.
 * <p>
 * Supports &#064;{@link org.eclipse.jetty.toolchain.test.annotation.Slow Slow} and 
 * &#064;{@link org.eclipse.jetty.toolchain.test.annotation.Stress Stress} supplemental
 * annotation on test methods to allow for filtering of what class of test to execute.
 * <p>
 * 
 * 
 * <pre>
 *    &#064;Test
 *    &#064;{@link org.eclipse.jetty.toolchain.test.annotation.Slow Slow}
 *    public void testLotsOfStuff() {
 *       ... do something long and complicated ...
 *    }
 *    
 *    &#064;Test
 *    &#064;{@link org.eclipse.jetty.toolchain.test.annotation.Stress Stress}("requirements to satisfy this test")
 *    public void testUsingLotsOfResources() {
 *       ... do something that uses lots of resources ...
 *    }
 *    
 *    &#064;Test
 *    public void testSimple() {
 *       ... do something that happens quickly ...
 *    }
 * </pre>
 * 
 * To enable / disable the various tests, you have some System properties you can utilize.
 * <p>
 * 
 * <dl>
 *   <dt>-Dtest.fast</dt>
 *   <dd>If present, this will disable &#064;{@link Slow}</dd>
 *   
 *   <dt>-Dtest.slow=(boolean)</dt>
 *   <dd>Enable or disable the &#064;{@link Slow} tests.<br>
 *   Default: true</dd>
 *   
 *   <dt>-Dtest.stress=(boolean)</dt>
 *   <dd>Enable or disable the &#064;{@link Stress} tests.<br>
 *   Default: false</dd>
 * </dl>
 */
public class AdvancedRunner extends BlockJUnit4ClassRunner
{
    private boolean slowTestsEnabled = false;
    private boolean stressTestsEnabled = false;

    public AdvancedRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        boolean isFast = PropertyFlag.isEnabled("test.fast");
        this.slowTestsEnabled = isEnabled("test.slow",!isFast);
        this.stressTestsEnabled = isEnabled("test.stress",false);
    }

    private boolean isEnabled(String key, boolean def)
    {
        String val = System.getProperty(key);
        if (val == null)
        {
            // not declared
            return def;
        }

        if (val.length() == 0)
        {
            // declared, but with no value (aka "-Dtest.slow")
            return true;
        }

        // declared, parse value
        return Boolean.parseBoolean(val);
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier)
    {
        Description description = describeChild(method);
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier,description);

        if (!slowTestsEnabled && method.getAnnotation(Slow.class) != null)
        {
            notify("@Slow (Ignored)",description);
            eachNotifier.fireTestIgnored();
            return;
        }

        if (!stressTestsEnabled && method.getAnnotation(Stress.class) != null)
        {
            Stress stress = method.getAnnotation(Stress.class);
            notify("@Stress (Ignored - " + stress.value() + ")",description);
            eachNotifier.fireTestIgnored();
            return;
        }
        
        if (method.getAnnotation(Ignore.class) != null) {
            notify("@Ignore",description);
            notifier.fireTestIgnored(description);
            return;
        }

        notify("Running", description);
        super.runChild(method,notifier);
    }

    private void notify(String msg, Description description)
    {
        System.err.printf("[AdvancedRunner] %s %s.%s()%n",msg,description.getClassName(),description.getMethodName());
    }
}
