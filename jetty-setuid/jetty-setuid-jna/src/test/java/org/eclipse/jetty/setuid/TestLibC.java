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

package org.eclipse.jetty.setuid;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestLibC
{
    @Test
    public void testSetuid() throws Exception
    {
        assertNull(LibC.INSTANCE.getpwnam("TheQuickBrownFoxJumpsOverToTheLazyDog"));
        assertNull(LibC.INSTANCE.getpwuid(-9999));

        // get the passwd info of root
        Passwd passwd1 = LibC.INSTANCE.getpwnam("root");
        // get the roots passwd info using the acquired uid
        Passwd passwd2 = LibC.INSTANCE.getpwuid(passwd1.getPwUid());

        assertEquals(passwd1.getPwName(), passwd2.getPwName());
        assertEquals(passwd1.getPwPasswd(), passwd2.getPwPasswd());
        assertEquals(passwd1.getPwUid(), passwd2.getPwUid());
        assertEquals(passwd1.getPwGid(), passwd2.getPwGid());
        assertEquals(passwd1.getPwGecos(), passwd2.getPwGecos());
        assertEquals(passwd1.getPwDir(), passwd2.getPwDir());
        assertEquals(passwd1.getPwShell(), passwd2.getPwShell());

        assertNull(LibC.INSTANCE.getgrnam("TheQuickBrownFoxJumpsOverToTheLazyDog"));
        assertNull(LibC.INSTANCE.getgrgid(-9999));

        // get the group using the roots groupid
        Group gr1 = LibC.INSTANCE.getgrgid(passwd1.getPwGid());
        // get the group name using the aquired name
        Group gr2 = LibC.INSTANCE.getgrnam(gr1.getGrName());

        assertEquals(gr1.getGrName(), gr2.getGrName());
        assertEquals(gr1.getGrPasswd(), gr2.getGrPasswd());
        assertEquals(gr1.getGrGid(), gr2.getGrGid());

        // search and check through membership lists
        if (gr1.getGrMem() != null)
        {
            assertEquals(gr1.getGrMem().length, gr2.getGrMem().length);
            for (int i = 0; i < gr1.getGrMem().length; i++)
            {
                assertEquals(gr1.getGrMem()[i], gr2.getGrMem()[i]);
            }
        }

        long oldSoftLimit;
        RLimit limit = new RLimit();
        assertEquals(0, LibC.INSTANCE.getrlimit(LibC.Constants.RLIMIT_NOFILE, limit));
        oldSoftLimit = limit._soft;
        limit._soft--;
        assertEquals(0, LibC.INSTANCE.setrlimit(LibC.Constants.RLIMIT_NOFILE, limit));
        assertEquals(0, LibC.INSTANCE.getrlimit(LibC.Constants.RLIMIT_NOFILE, limit));
        System.out.println(limit);
        assertEquals(oldSoftLimit - 1, limit._soft);
    }
}
