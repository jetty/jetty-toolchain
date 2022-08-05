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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UrlEncodeTest
{
    public static Stream<Arguments> getArguments()
    {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("<", "%3C"),
                Arguments.of(">", "%3E"),
                Arguments.of(":", "%3A"),
                Arguments.of("\"", "%22"),
                Arguments.of("/", "%2F"),
                Arguments.of("\\", "%5C"),
                Arguments.of("|", "%7C"),
                Arguments.of("?", "%3F"),
                Arguments.of("*", "%2A"),
                Arguments.of("\000", "%00"),
                Arguments.of("\032", "%1A"),
                Arguments.of("\177", "%7F")
        );
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void test(String value, String expected) throws Exception
    {
        assertThat(URLEncode.encode(value, StandardCharsets.UTF_8.name()), is(expected));
    }
}
