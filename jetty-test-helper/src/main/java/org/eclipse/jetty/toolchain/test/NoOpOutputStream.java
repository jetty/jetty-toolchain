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

import java.io.IOException;
import java.io.OutputStream;

/**
 * Do nothing OutputStream implementation
 */
class NoOpOutputStream extends OutputStream
{
    @Override
    public void write(byte[] b) throws IOException
    {
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
    }

    @Override
    public void flush() throws IOException
    {
    }

    @Override
    public void close() throws IOException
    {
    }

    @Override
    public void write(int b) throws IOException
    {
    }
}
