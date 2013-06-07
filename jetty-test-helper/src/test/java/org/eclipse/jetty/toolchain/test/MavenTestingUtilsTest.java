//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class MavenTestingUtilsTest
{
    @Test
    public void testGetTargetDir()
    {
        File dir = MavenTestingUtils.getTargetDir();
        Assert.assertEquals("target",dir.getName());
    }

    @Test
    public void testGetTargetFileBasic() throws IOException
    {
        File dir = MavenTestingUtils.getTargetDir();

        // File in .../target/pizza.log
        File expected = new File(dir,"pizza.log");
        FS.touch(expected);

        File actual = MavenTestingUtils.getTargetFile("pizza.log");
        assertSamePath(expected,actual);
    }

    @Test
    public void testGetTargetFileDeep() throws IOException
    {
        File dir = MavenTestingUtils.getTargetDir();
        File pizzadir = new File(dir,"pizza");
        FS.ensureDirExists(pizzadir);

        // File in .../target/pizza/pizza.receipt
        File expected = new File(pizzadir,"pizza.receipt");
        FS.touch(expected);

        // Should automatically adjust deep path
        File actual = MavenTestingUtils.getTargetFile("pizza/pizza.receipt");
        assertSamePath(expected,actual);
    }

    @Test
    public void testGetTestResourceFileSimple()
    {
        File dir = MavenTestingUtils.getTestResourcesDir();
        File expected = new File(dir,"dessert.txt");
        File actual = MavenTestingUtils.getTestResourceFile("dessert.txt");
        assertSamePath(expected,actual);
    }

    @Test
    public void testGetTestResourceFileDeep()
    {
        File dir = MavenTestingUtils.getTestResourcesDir();
        File breakfast = new File(dir,"breakfast");
        File expected = new File(breakfast,"eggs.txt");
        File actual = MavenTestingUtils.getTestResourceFile("breakfast/eggs.txt");
        assertSamePath(expected,actual);
    }

    @Test
    public void testGetTargetURL() throws Exception
    {
        File dir = MavenTestingUtils.getTargetDir();

        // File in .../target/pizza.log
        File expected = new File(dir,"url.log");
        FS.touch(expected);
        
        File actual = MavenTestingUtils.getTargetFile("url.log");
        
        URL url = MavenTestingUtils.getTargetURL("url.log");
        
        Assert.assertEquals( actual.toURI().toURL().toExternalForm(), url.toExternalForm());        
    }
    
    @Test
    public void testGetTestResourceDir()
    {
        File dir = MavenTestingUtils.getTestResourcesDir();
        File expected = new File(dir,"breakfast");
        File actual = MavenTestingUtils.getTestResourceDir("breakfast");
        assertSamePath(expected,actual);
    }
    
    private void assertSamePath(File expected, File actual)
    {
        Assert.assertEquals(expected.getAbsolutePath(),actual.getAbsolutePath());
    }
}
