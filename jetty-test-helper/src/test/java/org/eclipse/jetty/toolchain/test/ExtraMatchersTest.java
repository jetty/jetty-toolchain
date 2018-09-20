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

package org.eclipse.jetty.toolchain.test;

import static org.eclipse.jetty.toolchain.test.ExtraMatchers.*;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

@SuppressWarnings("javadoc")
public class ExtraMatchersTest
{
    @Test
    public void testOrderedFail()
    {
        List<String> actual = Arrays.asList(new String[] { "avocado", "banana", "cherry" });
        List<String> expected = Arrays.asList(new String[] { "apple", "banana", "cherry" });

        assertThrows(AssertionError.class, ()-> assertThat("Order", actual, ordered(expected)));
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
        
        assertThrows(AssertionError.class, ()-> assertThat("Order", actual, ordered(expected)));
    }

    @Test
    public void testRegexMatch_Simple()
    {
        assertThat("Regex", "Apple Pie", regex("Apple.*"));
        assertThat("Regex", "Cherry Pie", not(regex("Apple.*")));
    }

    @Test
    public void testRegexMatch_Pattern()
    {
        assertThat("Regex", "Apple Pie", regex(".*p{2,}.*"));
        assertThat("Regex", "Cherry Pie", not(regex(".*p{2,}.*")));
    }

    @Test
    public void testRegexMatch_Pattern_Or()
    {
        assertThat("Regex", "Apple Pie", regex("^(Apple|Cherry).*"));
        assertThat("Regex", "Cherry Pie", regex("^(Apple|Cherry).*"));
        assertThat("Regex", "Blueberry Pie", not(regex("^(Apple|Cherry).*")));
    }

    @Test
    public void testGreaterThan()
    {
        // Silly test ensures that existing hamcrest usage continues to work (re: junit 5 upgrade)
        assertThat("Value", 100, greaterThan(50));
    }
}
