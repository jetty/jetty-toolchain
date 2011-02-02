package org.eclipse.jetty.toolchain.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestName;
import org.junit.rules.Verifier;

public class StressTest
{
    private String result = null;
    private String expected = null;
    
    @Rule
    public TestName name = new TestName();

    @Rule
    public MethodRule verifier = new Verifier()
    {
        protected void verify() throws Throwable
        {
            Assert.assertEquals("Result", expected, result);
        }
    };

    @Test
    public void testEnabledDefault()
    {
        expected = "enabled-" + name.getMethodName();
        System.setProperty("STRESS","");
        Stress.assume(); // Should pass
        result = "enabled-" + name.getMethodName();
    }

    @Test
    public void testEnabledTrue()
    {
        expected = "enabled-" + name.getMethodName();
        System.setProperty("STRESS","true");
        Stress.assume(); // Should pass
        result = "enabled-" + name.getMethodName();
    }
    
    @Test
    public void testDisabledUnset()
    {
        expected = null;
        System.clearProperty("STRESS");
        Stress.assume(); // Should not pass
        result = "disabled-failure-" + name.getMethodName();
    }

    @Test
    public void testDisabledFalse()
    {
        expected = null;
        System.setProperty("STRESS","false");
        Stress.assume(); // Should not pass
        result = "disabled-failure-" + name.getMethodName();
    }
}
