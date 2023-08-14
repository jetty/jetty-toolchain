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

import com.sun.jna.Structure;

/**
 * Class is the equivalent java class used for holding values from native c code structure passwd. for more information please see man pages for getpwuid and getpwnam
 *   struct passwd {
 *        char    *pw_name;      // user name
 *        char    *pw_passwd;   // user password
 *        uid_t   pw_uid;         // user id
 *        gid_t   pw_gid;         // group id
 *        char    *pw_gecos;     // real name
 *        char    *pw_dir;        // home directory
 *       char    *pw_shell;      // shell program
 *    };
 *
 */
@Structure.FieldOrder({"_pwName", "_pwPasswd", "_pwUid", "_pwGid", "_pwGecos", "_pwDir", "_pwShell"})
public class Passwd extends Structure
{
    public String _pwName; /* user name */
    public String _pwPasswd; /* user password */
    public int _pwUid; /* user id */
    public int _pwGid; /* group id */
    public String _pwGecos; /* real name */
    public String _pwDir; /* home directory */
    public String _pwShell; /* shell program */

    public String getPwName()
    {
        return _pwName;
    }
    
    public String getPwPasswd()
    {
        return _pwPasswd;
    }
    
    public int getPwUid()
    {
        return _pwUid;
    }
    
    public int getPwGid()
    {
        return _pwGid;
    }
    
    public String getPwGecos()
    {
        return _pwGecos;
    }
    
    public String getPwDir()
    {
        return _pwDir;
    }
    
    public String getPwShell()
    {
        return _pwShell;
    }
}
