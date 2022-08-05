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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;

import org.junit.jupiter.api.Test;

public class NetTest
{
    @Test
    public void testIsInterfaceAvailableFor()
    {
        System.out.println("Net.isInterfaceAvailableFor(InetAddress) = " + Net.isInterfaceAvailableFor(InetAddress.class));
        System.out.println("Net.isInterfaceAvailableFor(Inet6Address) = " + Net.isInterfaceAvailableFor(Inet6Address.class));
        System.out.println("Net.isInterfaceAvailableFor(Inet4Address) = " + Net.isInterfaceAvailableFor(Inet4Address.class));
    }
}
