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

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * Class is the equivalent java class used for holding values from native c code structure group. for more information please see man pages for getgrnam and getgrgid
 * struct group {
 *             char   *gr_name;        // group name 
 *             char   *gr_passwd;     // group password
 *             gid_t   gr_gid;          // group ID 
 *             char  **gr_mem;        //  group members 
 *         };
 *
 */
@Structure.FieldOrder({"_grName", "_grPasswd", "_grGid", "_grMem"})
public class Group extends Structure
{
    public String _grName; /* group name */
    public String _grPasswd; /* group password */
    public int _grGid; /* group id */
    public Pointer _grMem; /* group members */


    public String getGrName()
    {
        return _grName;
    }

    public String getGrPasswd()
    {
        return _grPasswd;
    }

    public int getGrGid()
    {
        return _grGid;
    }

    public String[] getGrMem()
    {
        return _grMem.getStringArray(0);
    }
}
