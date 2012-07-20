package org.eclipse.jetty.toolchain.test;

import org.eclipse.jetty.toolchain.test.annotation.Slow;
import org.eclipse.jetty.toolchain.test.annotation.Stress;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

/**
 * Tiered Junit 4 Test Runner.
 * <p>
 * Supports &#064;LongTest annotation on test methods to only have those tests run during a Long test run.
 * <p>
 * 
 * <pre>
 *    &#064;Test
 *    &#064;{@link org.eclipse.jetty.toolchain.test.annotation.Slow Slow}
 *    public void testLotsOfStuff() {
 *       ... do something long and complicated ...
 *    }
 *    
 *    &#064;Test
 *    &#064;{@link org.eclipse.jetty.toolchain.test.annotation.Stress Stress}
 *    public void testUsingLotsOfResources() {
 *       ... do something that uses lots of resources ...
 *    }
 *    
 *    &#064;Test
 *    public void testSimple() {
 *       ... do something that happens quickly ...
 *    }
 * </pre>
 */
public class AdvancedRunner extends BlockJUnit4ClassRunner
{
    private boolean slowTestsEnabled = false;
    private boolean stressTestsEnabled = false;

    public AdvancedRunner(Class<?> klass) throws InitializationError
    {
        super(klass);
        boolean isFast = (System.getProperty("test.fast") != null);
        this.slowTestsEnabled = !isFast || isEnabled("test.slow");
        this.stressTestsEnabled = isEnabled("test.stress");
    }

    private boolean isEnabled(String key)
    {
        String val = System.getProperty(key);
        if (val == null)
        {
            // not declared
            return false;
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
        EachTestNotifier eachNotifier = makeNotifier(method,notifier);

        if (!slowTestsEnabled && method.getAnnotation(Slow.class) != null)
        {
            eachNotifier.fireTestIgnored();
            return;
        }

        if (!stressTestsEnabled && method.getAnnotation(Stress.class) != null)
        {
            eachNotifier.fireTestIgnored();
            return;
        }

        super.runChild(method,notifier);
    }

    private EachTestNotifier makeNotifier(FrameworkMethod method, RunNotifier notifier)
    {
        Description description = describeChild(method);
        return new EachTestNotifier(notifier,description);
    }
}
