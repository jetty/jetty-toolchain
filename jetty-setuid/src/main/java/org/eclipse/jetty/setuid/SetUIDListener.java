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

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.LifeCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This LifeCycleListener may be added to a {@link Server} to make a JNI call to set the unix UID.</p>
 * <p>This can be used to start the server as root so that privileged ports may
 * be accessed and then switch to a non-root user for security. Depending on
 * the value of {@link #setStartServerAsPrivileged(boolean)}, either the server
 * will be started and then the UID set; or the {@link Server#getConnectors()}
 * will be opened, the UID set and then the server is started. The latter is
 * the default and avoids any web application code being run as a privileged
 * user, but will not work if the application code also needs to open
 * privileged ports.</p>
 * <p>The configured umask is set before the server is started and the
 * configured gid/uid is set after the server is started.</p>
 */
public class SetUIDListener implements LifeCycle.Listener
{
    private static final Logger LOG = LoggerFactory.getLogger(SetUIDListener.class);

    private int _uid = 0;
    private int _gid = 0;
    private int _umask = -1;
    private boolean _startServerAsPrivileged;
    private boolean _clearSupplementalGroups;
    private RLimit _rlimitNoFiles = null;

    public void setUsername(String username)
    {
        Passwd passwd = LibC.INSTANCE.getpwnam(username);
        _uid = passwd.getPwUid();
    }

    public String getUsername()
    {
        Passwd passwd = LibC.INSTANCE.getpwuid(_uid);
        return passwd.getPwName();
    }

    public void setGroupname(String groupname)
    {
        Group group = LibC.INSTANCE.getgrnam(groupname);
        _gid = group.getGrGid();
    }

    public String getGroupname()
    {
        Group group = LibC.INSTANCE.getgrgid(_gid);
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

    protected void setGidUid()
    {
        if (_gid != 0)
        {
            if (isClearSupplementalGroups())
            {
                LOG.info("Clearing supplemental groups");
                LibC.INSTANCE.setgroups(new int[0]);
            }
            LOG.info("Setting GID={}", _gid);
            LibC.INSTANCE.setgid(_gid);
        }
        if (_uid != 0)
        {
            LOG.info("Setting UID={}", _uid);
            LibC.INSTANCE.setuid(_uid);
            Passwd pw = LibC.INSTANCE.getpwuid(_uid);
            System.setProperty("user.name",pw.getPwName());
            System.setProperty("user.home",pw.getPwDir());
        }
    }


    public void lifeCycleFailure(LifeCycle lifecycle, Throwable cause)
    {

    }

    public void lifeCycleStarted(LifeCycle lifecycle)
    {
        if (_startServerAsPrivileged)
            setGidUid();
    }

    public void lifeCycleStarting(LifeCycle lifecycle)
    {
        String jettyUserEnvVariable = System.getenv("JETTY_USER");
        if (jettyUserEnvVariable != null)
            LOG.warn("JETTY_USER set to: {}. If JETTY_USER is set, starting jetty as root and using " +
                    "jetty-setuid to switch user won't work!!!", jettyUserEnvVariable);
        if (_umask > -1)
        {
            LOG.info("Setting umask=0{}", Integer.toString(_umask,8));
            LibC.INSTANCE.setumask(_umask);
        }

        if (_rlimitNoFiles != null)
        {
            if (LibC.Constants.RLIMIT_NOFILE == Integer.MIN_VALUE)
            {
                LOG.warn("rlimitNoFiles cannot be set on unsupported platform, ignoring configured values: " + _rlimitNoFiles);
            }
            else
            {
                RLimit temp = new RLimit();
                LibC.INSTANCE.getrlimit(LibC.Constants.RLIMIT_NOFILE, temp);
                LOG.info("Current {}", temp);
                int success = LibC.INSTANCE.setrlimit(LibC.Constants.RLIMIT_NOFILE, _rlimitNoFiles);
                if (success < 0)
                    LOG.warn("Failed to set rlimit_nofiles, returned status {}", success);
                LibC.INSTANCE.getrlimit(LibC.Constants.RLIMIT_NOFILE, temp);
                LOG.info("Set {}", temp);
            }
        }

        // If we are starting the server privileged, delay the setuid until started.
        if (_startServerAsPrivileged)
            return;

        // Start the thread pool and connectors
        try
        {
            Server server = (Server)lifecycle;
            if (server.getThreadPool() instanceof LifeCycle)
            {
                LifeCycle tplc = (LifeCycle)server.getThreadPool();
                if (!tplc.isRunning())
                {
                    // Start the Threadpool early, but make the server manage it
                    // Threadpool is responsible for processing the opening of the Connectors
                    server.manage(tplc);
                    tplc.start();
                }
            }

            Connector[] connectors = server.getConnectors();
            if (connectors!=null)
            {
                for (Connector connector : connectors)
                {
                    if (connector instanceof NetworkConnector)
                    {
                        // Open with Setuid permissions (eg: port 80)
                        ((NetworkConnector)connector).open();
                        LOG.info("Opened {}", connector);
                    }
                    else if (!connector.isRunning())
                    {
                        // Start with Setuid permissions (eg: port 80 or unixsocket)
                        server.manage(connector);
                        connector.start();
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        setGidUid();
    }

    public void lifeCycleStopped(LifeCycle ignore)
    {
        // ignore
    }

    public void lifeCycleStopping(LifeCycle ignore)
    {
        // ignore
    }
    
    /**
     * @return the startServerAsPrivileged
     */
    public boolean isStartServerAsPrivileged()
    {
        return _startServerAsPrivileged;
    }

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
