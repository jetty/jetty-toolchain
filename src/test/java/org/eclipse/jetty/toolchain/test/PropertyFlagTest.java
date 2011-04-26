package org.eclipse.jetty.toolchain.test;

import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestName;
import org.junit.rules.Verifier;

public class PropertyFlagTest
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
        PropertyFlag.assume("STRESS"); // Should pass
        result = "enabled-" + name.getMethodName();
        Assert.assertThat("Stress.enabled()", Stress.isEnabled(), is(true));
    }

    @Test
    public void testEnabledTrue()
    {
        expected = "enabled-" + name.getMethodName();
        System.setProperty("STRESS","true");
        PropertyFlag.assume("STRESS"); // Should pass
        result = "enabled-" + name.getMethodName();
        Assert.assertThat("Stress.enabled()", Stress.isEnabled(), is(true));
    }
    
    @Test
    public void testDisabledUnset()
    {
        expected = null;
        System.clearProperty("STRESS");
        Assert.assertThat("Stress.enabled()", Stress.isEnabled(), is(false));
        PropertyFlag.assume("STRESS"); // Should not pass
        result = "disabled-failure-" + name.getMethodName();
    }

    @Test
    public void testDisabledFalse()
    {
        expected = null;
        System.setProperty("STRESS","false");
        Assert.assertThat("Stress.enabled()", Stress.isEnabled(), is(false));
        PropertyFlag.assume("STRESS"); // Should not pass
        result = "disabled-failure-" + name.getMethodName();
    }
}
