package org.eclipse.jetty.toolchain.test;

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
    public void testSomethingSlow()
    {
        System.out.println("executed @Slow");
        for (int i = 0; i < 1000; i++)
        {
            Assert.assertEquals("m" + i,String.format("m%d",i));
        }
    }

    @Test
    @Stress("Requires at least -DforkMode=never -Xmx4g")
    public void testSomethingStressy()
    {
        System.out.println("executed @Stress");
        int len = 1024000;
        int numBuffers = 2700;
        byte buf[][] = new byte[numBuffers][];
        for(int i=0; i<numBuffers; i++) {
            buf[i] = new byte[len];
        }
        for(int i=0; i<numBuffers; i++) {
            Assert.assertEquals("buf[" + i + "].length", len, buf[i].length);
        }
    }

    @Test
    public void testNormal()
    {
        System.out.println("executed @Test");
        // should have run
        Assert.assertEquals("123",Integer.toString(123));
    }
}
