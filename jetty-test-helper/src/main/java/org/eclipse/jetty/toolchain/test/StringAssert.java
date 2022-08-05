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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

/**
 * Collection of common asserts for Strings.
 *
 * @deprecated use Hamcrest {@code assertThat()} techniques
 */
@Deprecated(forRemoval = true, since = "6.0")
public final class StringAssert
{
    private StringAssert()
    {
        /* prevent instantiation */
    }

    /**
     * Asserts that string (<code>haystack</code>) contains specified text (
     * <code>needle</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     * @deprecated use {@code assertThat(msg, haystack, containsString(needle));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertContains(String msg, String haystack, String needle)
    {
        assertThat(msg, haystack, containsString(needle));
    }

    /**
     * Asserts that string (<code>haystack</code>) contains specified text (
     * <code>needle</code>), starting at offset (in <code>haystack</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     * @param offset the offset in (haystack) to perform search from
     * @deprecated use {@code assertThat(msg, haystack.substring(offset), containsString(needle));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertContains(String msg, String haystack, String needle, int offset)
    {
        assertThat(msg, haystack.substring(offset), containsString(needle));
    }

    /**
     * Asserts that the list of String lines contains the same lines (without a regard for the order of those lines)
     *
     * @param msg the assertion message
     * @param linesExpected the list of expected lines
     * @param linesActual the list of actual lines
     * @deprecated use {@code assertThat(msg, linesActual, containsInAnyOrder(linesExpected.toArray()));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertContainsSame(String msg, List<String> linesExpected, List<String> linesActual)
    {
        assertThat(msg, linesActual, containsInAnyOrder(linesExpected.toArray()));
    }

    /**
     * Asserts that string (<code>haystack</code>) does <u>not</u> contain
     * specified text (<code>needle</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     * @deprecated use {@code assertThat(msg, haystack, not(containsString(needle)));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertNotContains(String msg, String haystack, String needle)
    {
        assertThat(msg, haystack, not(containsString(needle)));
    }

    /**
     * Asserts that string (<code>haystack</code>) does <u>not</u> contain
     * specified text (<code>needle</code>), starting at offset (in
     * <code>haystack</code>).
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param needle the text to search for
     * @param offset the offset in (haystack) to perform search from
     * @deprecated use {@code assertThat(msg, haystack.substring(offset), not(containsString(needle)));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertNotContains(String msg, String haystack, String needle, int offset)
    {
        assertThat(msg, haystack.substring(offset), not(containsString(needle)));
    }

    /**
     * Asserts that the string (<code>haystack</code>) starts with the string (
     * <code>expected</code>)
     *
     * @param msg the assertion message
     * @param haystack the text to search in
     * @param expected the expected starts with text
     * @deprecated use {@code assertThat(msg, haystack, startsWith(expected));} instead
     */
    @Deprecated(forRemoval = true, since = "6.0")
    public static void assertStartsWith(String msg, String haystack, String expected)
    {
        assertThat(msg, haystack, startsWith(expected));
    }
}
