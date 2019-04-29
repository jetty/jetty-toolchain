//
//  ========================================================================
//  Copyright (c) 1995-2019 Mort Bay Consulting Pty. Ltd.
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
