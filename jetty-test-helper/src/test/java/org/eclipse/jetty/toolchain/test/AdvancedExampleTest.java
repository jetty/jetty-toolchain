package org.eclipse.jetty.toolchain.test;

import org.eclipse.jetty.toolchain.test.annotation.Performance;
import org.eclipse.jetty.toolchain.test.annotation.Slow;
import org.eclipse.jetty.toolchain.test.annotation.Stress;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AdvancedRunner.class)
public class AdvancedExampleTest
{
    @Test
    @Slow
    public void testSomethingSlow() {
        Assert.fail("Shouldn't have run");
    }
    
    @Test
    @Stress
    public void testSomethingStressy() {
        Assert.fail("Shouldn't have run");
    }
    
    @Test
    @Performance
    public void testSomethingWithPerformance() {
        Assert.fail("Shouldn't have run");
    }
    
    @Test
    public void testExtra() {
        // should have run
        Assert.assertEquals("123", Integer.toString(123));
    }
}
