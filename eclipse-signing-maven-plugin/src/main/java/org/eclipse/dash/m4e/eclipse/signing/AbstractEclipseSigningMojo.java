package org.eclipse.dash.m4e.eclipse.signing;

//========================================================================
//Copyright (c) 2010-2015 Intalio, Inc.
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//The Eclipse Public License is available at 
//http://www.eclipse.org/legal/epl-v10.html
//The Apache License v2.0 is available at
//http://www.opensource.org/licenses/apache2.0.php
//You may elect to redistribute this code under either of these licenses. 
//========================================================================

import java.io.File;
import java.net.InetAddress;

import org.apache.maven.artifact.manager.WagonManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.settings.Settings;
import org.apache.maven.wagon.CommandExecutor;
import org.apache.maven.wagon.Wagon;
import org.codehaus.mojo.wagon.shared.WagonUtils;



/**
 * AbstractEclipseSigningMojo
 *
 *
 */
public abstract class AbstractEclipseSigningMojo extends AbstractMojo
{
    /**
     * @component
     */
    protected WagonManager wagonManager;

    /**
     * The current user system settings for use in Maven.
     * 
     * @parameter expression="${settings}"
     * @readonly
     */
    protected Settings settings;

    /**
     * protocol for the wagon connection
     * 
     * Example: scp://
     * 
     * @parameter
     */
    protected String wagonProtocol;

    /**
     * host portion of the wagon connection
     * 
     * Example: build.eclipse.org
     * 
     * @parameter
     */
    protected String wagonHost;

    /**
     * path portion of the wagon connection
     * 
     * Example: /home/data/users/jmcconnell
     * 
     * @parameter
     */
    protected String wagonPath;

    /**
     * @parameter
     */
    protected String serverId;

    /**
     * @parameter default-value="900000";
     * @required
     */
    protected int wagonTimeout;

    /**
     * 
     * @return whether or not we are running on an eclipse build machine
     * @throws Exception
     */
    protected boolean runningOnBuildMachine() throws Exception
    {
        if (InetAddress.getLocalHost().getHostName().equals("build.eclipse.org"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * @param location of wagon target
     * @return adjusted wagon path
     */
    protected String adjustToWagonPath(String location)
    {
        return wagonPath + File.separator + location;
    }



    /**
     * @return synthesize the url for wagon
     * @throws Exception
     */
    protected String getWagonUrl() throws Exception
    {
        if (wagonProtocol == null || wagonHost == null || wagonPath == null)
        {
            throw new IllegalArgumentException("missing wagon configuration bits, unable to operate remotely");
        }

        if (runningOnBuildMachine())
        {
            return wagonProtocol + wagonPath;
        }

        return wagonProtocol + wagonHost + wagonPath;
    }



    /**
     * @param id 
     * @param url
     * @return wagon instance
     * @throws MojoExecutionException
     */
    protected Wagon createWagon(String id, String url) throws MojoExecutionException
    {
        try
        {
            return WagonUtils.createWagon(id,url,wagonManager,settings,this.getLog());
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Unable to create a Wagon instance for " + url,e);
        }

    }


    /**
     * @param wagon the wagon instance
     * @return a commandexecutor
     * @throws Exception
     */
    protected CommandExecutor getCommandExecutor(Wagon wagon) throws Exception
    {
        if (!(wagon instanceof CommandExecutor))
        {
            throw new MojoExecutionException("unable to operate remotely, requires wagon capable of invoking commands");
        }

        CommandExecutor exec = (CommandExecutor)wagon;

        exec.setTimeout(wagonTimeout);

        return exec;
    }
}
