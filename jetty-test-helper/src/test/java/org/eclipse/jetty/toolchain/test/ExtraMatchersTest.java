//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
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

import static org.eclipse.jetty.toolchain.test.ExtraMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

@SuppressWarnings("javadoc")
public class ExtraMatchersTest
{
    @Rule
    public ExpectedException expectedThrowable = ExpectedException.none();
    
    @Test
    public void testOrderedFail()
    {
        List<String> actual = Arrays.asList(new String[] { "avocado", "banana", "cherry" });
        List<String> expected = Arrays.asList(new String[] { "apple", "banana", "cherry" });
        
        expectedThrowable.expect(AssertionError.class);
        assertThat("Order", actual, ordered(expected));
    }
    
    @Test
    public void testOrderedSuccess()
    {
        List<String> actual = Arrays.asList(new String[] { "apple", "banana", "cherry" });
        List<String> expected = Arrays.asList(new String[] { "apple", "banana", "cherry" });
        
        assertThat("Order", actual, ordered(expected));
    }
    
    @Test
    public void testOrderedMismatch()
    {
        List<String> actual = Arrays.asList(new String[] { "banana", "apple", "cherry" });
        List<String> expected = Arrays.asList(new String[] { "apple", "banana", "cherry" });
        
        expectedThrowable.expect(AssertionError.class);
        assertThat("Order", actual, ordered(expected));
    }
}
