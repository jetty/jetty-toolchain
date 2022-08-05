//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.toolchain.test;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.eclipse.jetty.toolchain.test.ExtraMatchers.ordered;
import static org.eclipse.jetty.toolchain.test.ExtraMatchers.regex;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
