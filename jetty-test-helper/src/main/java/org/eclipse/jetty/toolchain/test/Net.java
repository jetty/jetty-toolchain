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
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class Net
{
    public static boolean isInterfaceAvailableFor(Class<? extends InetAddress> addrClass)
    {
        try
        {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements())
            {
                NetworkInterface iface = ifaces.nextElement();
                Enumeration<InetAddress> addrs = iface.getInetAddresses();
                while (addrs.hasMoreElements())
                {
                    InetAddress addr = addrs.nextElement();
                    if (addrClass.isAssignableFrom(addr.getClass()))
                    {
                        return true;
                    }
                }
            }
        }
        catch (SocketException ignore)
        {
        }
        return false;
    }

    public static boolean isIpv6InterfaceAvailable()
    {
        return isInterfaceAvailableFor(Inet6Address.class);
    }

    public static boolean isIpv4InterfaceAvailable()
    {
        return isInterfaceAvailableFor(Inet4Address.class);
    }
}
