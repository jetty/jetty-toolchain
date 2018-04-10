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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Testing Junit Jupiter behavior with {@link WorkDir} and {@link WorkDirExtension}
 */
@ExtendWith(WorkDirExtension.class)
public class WorkDirExtensionTest
{
    @WorkDir
    public Path fieldDir;

    @Test
    public void testWorkDir_AsField()
    {
        assertThat("testingDir", fieldDir, is(notNullValue()));
    }

    @Test
    public void testWorkDir_AsParam(@WorkDir Path dir)
    {
        assertThat("testingDir", dir, is(notNullValue()));
    }
}

