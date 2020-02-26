//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.setuid;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

/**
 * @deprecated use {@link SetUIDListener} instead
 */
@Deprecated
public class SetUIDServer extends Server
{
    private static final Logger LOG = Log.getLogger(SetUIDServer.class);

    private int _uid = 0;
    private int _gid = 0;
    private int _umask = -1;
    private boolean _startServerAsPrivileged;
    private boolean _clearSupplementalGroups;
    private RLimit _rlimitNoFiles = null;

    public void setUsername(String username)
    {
        Passwd passwd = SetUID.getpwnam(username);
        _uid = passwd.getPwUid();
    }

    public String getUsername()
    {
        Passwd passwd = SetUID.getpwuid(_uid);
        return passwd.getPwName();
    }

    public void setGroupname(String groupname)
    {
        Group group = SetUID.getgrnam(groupname);
        _gid = group.getGrGid();
    }

    public String getGroupname()
    {
        Group group = SetUID.getgrgid(_gid);
        return group.getGrName();
    }

    public int getUmask()
    {
        return _umask;
    }

    public String getUmaskOctal()
    {
        return Integer.toOctalString(_umask);
    }

    public void setUmask(int umask)
    {
        _umask = umask;
    }

    public void setUmaskOctal(String umask)
    {
        _umask = Integer.parseInt(umask,8);
    }

    public int getUid()
    {
        return _uid;
    }

    public void setUid(int uid)
    {
        _uid = uid;
    }

    public void setGid(int gid)
    {
        _gid = gid;
    }

    public int getGid()
    {
        return _gid;
    }

    public void setRLimitNoFiles(RLimit rlimit)
    {
        _rlimitNoFiles = rlimit;
    }

    public RLimit getRLimitNoFiles()
    {
        return _rlimitNoFiles;
    }

    public boolean isClearSupplementalGroups()
    {
        return _clearSupplementalGroups;
    }

    public void setClearSupplementalGroups(boolean clearSupplementalGroups)
    {
        _clearSupplementalGroups = clearSupplementalGroups;
    }

    @Override
    protected void doStart() throws Exception
    {
        String jettyUserEnvVariable = System.getenv("JETTY_USER");
        if (jettyUserEnvVariable != null)
            LOG.warn("JETTY_USER set to: {}. If JETTY_USER is set, starting jetty as root and using " +
                    "jetty-setuid to switch user won't work!!!", jettyUserEnvVariable);
        if (_umask > -1)
        {
            LOG.info("Setting umask=0" + Integer.toString(_umask,8));
            SetUID.setumask(_umask);
        }

        if (_rlimitNoFiles != null)
        {
            LOG.info("Current " + SetUID.getrlimitnofiles());
            int success = SetUID.setrlimitnofiles(_rlimitNoFiles);
            if (success < 0)
                LOG.warn("Failed to set rlimit_nofiles, returned status " + success);
            LOG.info("Set " + SetUID.getrlimitnofiles());
        }

        if (_startServerAsPrivileged)
        {
            super.doStart();
            if (_gid != 0)
            {
                if (isClearSupplementalGroups())
                {
                    LOG.info("Clearing supplemental groups");
                    SetUID.setgroups(new int[0]);
                }
                LOG.info("Setting GID=" + _gid);
                SetUID.setgid(_gid);
            }
            if (_uid != 0)
            {
                LOG.info("Setting UID=" + _uid);
                SetUID.setuid(_uid);
                Passwd pw = SetUID.getpwuid(_uid);
                System.setProperty("user.name",pw.getPwName());
                System.setProperty("user.home",pw.getPwDir());
            }
        }
        else
        {
            Connector[] connectors = getConnectors();
            for (int i = 0; connectors != null && i < connectors.length; i++)
            {
                connectors[i].start();
            }
            
            if (_gid != 0)
            {
                if (isClearSupplementalGroups())
                {
                    LOG.info("Clearing supplemental groups");
                    SetUID.setgroups(new int[0]);
                }
                LOG.info("Setting GID=" + _gid);
                SetUID.setgid(_gid);
            }
            
            if (_uid != 0)
            {
                LOG.info("Setting UID=" + _uid);
                SetUID.setuid(_uid);
                Passwd pw = SetUID.getpwuid(_uid);
                System.setProperty("user.name",pw.getPwName());
                System.setProperty("user.home",pw.getPwDir());
            }
            
            super.doStart();
        }
    }

    /* ------------------------------------------------------------ */
    /**
     * @return the startServerAsPrivileged
     */
    public boolean isStartServerAsPrivileged()
    {
        return _startServerAsPrivileged;
    }

    /* ------------------------------------------------------------ */
    /**
     * @param startContextsAsPrivileged
     *            if true, the server is started and then the process UID is switched. If false, the connectors are opened, the UID is switched and then the
     *            server is started.
     */
    public void setStartServerAsPrivileged(boolean startContextsAsPrivileged)
    {
        _startServerAsPrivileged = startContextsAsPrivileged;
    }
}
