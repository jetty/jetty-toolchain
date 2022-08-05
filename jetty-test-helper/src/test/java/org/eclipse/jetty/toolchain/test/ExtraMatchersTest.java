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

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.eclipse.jetty.toolchain.test.ExtraMatchers.ordered;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("javadoc")
public class ExtraMatchersTest
{
    @Test
    public void testOrderedFail()
    {
        List<String> actual = List.of("avocado", "banana", "cherry");
        List<String> expected = List.of("apple", "banana", "cherry");

        assertThrows(AssertionError.class, () -> assertThat("Order", actual, ordered(expected)));
    }

    @Test
    public void testOrderedSuccess()
    {
        List<String> actual = List.of("apple", "banana", "cherry");
        List<String> expected = List.of("apple", "banana", "cherry");

        assertThat("Order", actual, ordered(expected));
    }

    @Test
    public void testOrderedMismatch()
    {
        List<String> actual = List.of("banana", "apple", "cherry");
        List<String> expected = List.of("apple", "banana", "cherry");

        assertThrows(AssertionError.class, () -> assertThat("Order", actual, ordered(expected)));
    }
}
