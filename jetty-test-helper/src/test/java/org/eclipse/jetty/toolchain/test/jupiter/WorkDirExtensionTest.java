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

package org.eclipse.jetty.toolchain.test.jupiter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

/**
 * Testing Junit Jupiter behavior with {@link WorkDir} and {@link WorkDirExtension}
 */
@ExtendWith({WorkDirExtension.class})
public class WorkDirExtensionTest
{
    public WorkDir fieldDir;

    @Test
    public void testWorkDir_AsField()
    {
        assertThat("testingDir", fieldDir, is(notNullValue()));
        assertThat("testingDir.getPath()", fieldDir.getPath(), is(notNullValue()));
    }

    @Test
    public void testWorkDir_AsParam(WorkDir dir)
    {
        assertThat("testingDir", dir, is(notNullValue()));
        assertThat("testingDir.getPath()", dir.getPath(), is(notNullValue()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"foo", "bar", "a longer\nstring\r\bwith\tcontrol\0characters"})
    public void testWorkDir_WithParameterized(String val)
    {
        fieldDir.getPath();
    }

    @Test
    public void testWorkDir_TestInfo(TestInfo testInfo)
    {
        fieldDir.getPath();
    }

    private static Stream<Arguments> testValues()
    {
        return Stream.of(
            Arguments.of("", "", null),
            Arguments.of(":80", "", "80"),
            Arguments.of("host", "host", null)
        );
    }

    @ParameterizedTest
    @MethodSource("testValues")
    public void testWorkDir_WithParameterizedMethodSource(String a, String b, String c)
    {
        fieldDir.getPath();
    }
}

