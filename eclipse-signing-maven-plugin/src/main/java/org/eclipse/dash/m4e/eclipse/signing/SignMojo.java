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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.wagon.CommandExecutor;
import org.apache.maven.wagon.Streams;
import org.apache.maven.wagon.Wagon;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.archiver.util.DefaultFileSet;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.components.io.fileselectors.FileInfo;
import org.codehaus.plexus.components.io.fileselectors.FileSelector;
import org.codehaus.plexus.util.FileUtils;

/**
 * 
 * @goal sign
 * @phase package
 * @description runs the eclipse signing process
 */
public class SignMojo extends AbstractEclipseSigningMojo
{

    /**
     * Accepted values are 'local' and 'remote'
     * 
     * @parameter default-value="local"
     * @required
     */
    protected String execute;

    /**
     * zip file to get signed
     * 
     * @parameter default-value= "${project.build.directory}/packed/site_assembly.zip"
     * @required
     */
    protected String inputFile;

    /**
     * zip file to get signed
     * 
     * @parameter default-value= "${project.build.directory}/signed/site_assembly.zip"
     * @required
     */
    protected String outputFile;

    /**
     * directory to send artifact to be signed
     * 
     * should be akin to: /home/data/httpd/download-staging.priv/rt/&lt;project&gt;
     * 
     * @parameter
     * @required
     */
    protected String signerInputDirectory;

    /**
     * directory monitor for signed artifact
     * 
     * @parameter
     */
    protected String signerOutputDirectory;

    /**
     * directory monitor interval for signed artifact
     * 
     * @parameter default-value="60000"
     * @required
     */
    protected int zipCheckInterval;

    /**
     * directory monitor for signed artifact
     * 
     * @parameter default-value="30"
     * @required
     */
    protected int maxZipChecks;

    /** 
     * @component 
     */
    protected PlexusContainer plexus;

    public void execute() throws MojoExecutionException, MojoFailureException
    {

        if (!FileUtils.fileExists(inputFile))
            throw new MojoExecutionException("zip file does not exist - " + inputFile);

        try
        {

            if (!FileUtils.fileExists(FileUtils.dirname(outputFile)))
            {
                info("creating directory to hold signed artifact");
                FileUtils.mkdir(FileUtils.dirname(outputFile));
            }

            System.err.println("input file " + inputFile);

            // remove the *.pack.gz if any:
            File in = new File(inputFile);
            ZipUnArchiver unarchive = (ZipUnArchiver)plexus.lookup(ZipUnArchiver.ROLE,"zip");
            File dest = new File(in.getParentFile(),"repository");
            dest.mkdir();
            unarchive.setDestDirectory(dest);
            unarchive.setSourceFile(in);
            unarchive.setFileSelectors(new FileSelector[]
            { new FileSelector()
            {
                public boolean isSelected(FileInfo arg0) throws IOException
                {
                    return !arg0.getName().endsWith(".pack.gz");
                }

            } });
            System.err.println(unarchive.getSourceFile() + " - unarchived into - " + unarchive.getDestDirectory());
            System.err.println(unarchive.getSourceFile().getAbsolutePath() + " unarchived into " + unarchive.getDestDirectory().getAbsolutePath());
            unarchive.extract();// path, new File(in.getParentFile(), "repository"));

            FileUtils.rename(in,new File(in.getParentFile(),in.getName() + "-before-removing-pack-gz.zip"));

            ZipArchiver archiver = (ZipArchiver)plexus.lookup(ZipArchiver.ROLE,"zip");// new ZipArchiver();
            // archiver.addDirectory(unarchive.getDestDirectory(), "repository");//, new String[] {"**/*.jar"}, new String[] {"**/*.pack.gz"});
            DefaultFileSet fileset = new DefaultFileSet();
            fileset.setDirectory(unarchive.getDestDirectory());
            fileset.setFileSelectors(new FileSelector[]
            { new FileSelector()
            {
                public boolean isSelected(FileInfo arg0) throws IOException
                {
                    System.err.println("looking at " + arg0.getName());
                    return !arg0.getName().endsWith(".pack.gz");
                }

            } });
            archiver.addFileSet(fileset);
            File tmpDest = new File(in.getParentFile(),in.getName() + "-after-removing.zip");
            archiver.setDestFile(tmpDest);
            System.err.println("....archive " + unarchive.getDestDirectory() + " into " + new File(inputFile).getAbsolutePath());
            archiver.createArchive();
            FileUtils.copyFile(tmpDest,in);

            if ("local".equals(execute))
            {
                signLocally();
            }
            else if ("remote".equals(execute))
            {
                signRemotely();
            }

        }
        catch (Exception e)
        {
        	getLog().error(e);
        	if (e instanceof MojoExecutionException)
        	{
        		throw (MojoExecutionException)e;
        	}
        	if (e instanceof MojoFailureException)
        	{
        		throw (MojoFailureException)e;
        	}
        }
    }

    private void signLocally() throws Exception
    {
        signerInputDirectory = signerInputDirectory + File.separator + UUID.randomUUID().toString();

        signerOutputDirectory = signerInputDirectory + File.separator + "signed";

        FileUtils.mkdir(signerInputDirectory);
        
        if (!FileUtils.fileExists(signerInputDirectory))
        {
            throw new MojoFailureException("Unable to create the directory " + signerInputDirectory);
        }

        FileUtils.mkdir(signerOutputDirectory);
        FileUtils.copyFile(new File(inputFile),new File(signerInputDirectory + File.separator + FileUtils.filename(inputFile)));

        Process pc = Runtime.getRuntime().exec(new String[]
        { "/bin/chmod", "-R", "ugo+rw", signerInputDirectory });
        pc.waitFor();

        Process ps = Runtime.getRuntime().exec(new String[]
        { "/usr/bin/sign", signerInputDirectory + File.separator + FileUtils.filename(inputFile), "nomail", signerOutputDirectory });

        BufferedReader is = new BufferedReader(new InputStreamReader(ps.getInputStream()));

        String liner;
        while ((liner = is.readLine()) != null)
            System.out.println(liner);

        System.out.println("In Main after EOF");
        System.out.flush();
        try
        {
            ps.waitFor(); // wait for process to complete
        }
        catch (InterruptedException e)
        {
            System.err.println(e); // "Can'tHappen"
            return;
        }

        boolean keepChecking = true;
        int count = 0;
        while (keepChecking && count < maxZipChecks)
        {
            try
            {
                info("monitoring for signed file in " + signerOutputDirectory);
                ++count;
                Thread.sleep(zipCheckInterval);

                
                @SuppressWarnings("unchecked")
                List<String> signedDirList = FileUtils.getFileNames(new File(signerOutputDirectory),"**",null,true);

                info("signed dir output: " + signedDirList.size());

                for (String line : signedDirList)
                {
                    getLog().info("-> " + line);
                }

                if (signedDirList.size() == 1) // . .. signed file
                {
                    // if ( FileUtils.filename(toSignZipFile).equals(signedDirList.get(0)))
                    // {
                    info("found the signed file");
                    keepChecking = false;
                    // }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (count == maxZipChecks)
        {
            getLog().error("signer process never signaled completion");
            FileUtils.deleteDirectory(signerInputDirectory);
            return;
        }
        else
        {
            getLog().info("copying signed artifact to :" + outputFile);
            FileUtils.copyFile(new File(signerOutputDirectory + File.separator + FileUtils.filename(inputFile)),new File(outputFile));
            FileUtils.deleteDirectory(signerInputDirectory);
            return;
        }

    }

    private void signRemotely() throws Exception
    {
        Wagon wagon = createWagon(serverId,getWagonUrl());

        CommandExecutor exec = getCommandExecutor(wagon);

        signerInputDirectory = UUID.randomUUID().toString();

        signerOutputDirectory = signerInputDirectory + File.separator + "signed";

        info("creating remote input directory");
        exec.executeCommand("/bin/mkdir -p " + signerInputDirectory);

        info("setting permissions");
        exec.executeCommand("/bin/mkdir -p " + signerOutputDirectory);

        info("copying file to sign to remote directory");
        wagon.put(new File(inputFile),signerInputDirectory + File.separator + FileUtils.filename(inputFile));

        info("setting permissions on file");
        exec.executeCommand("/bin/chmod -R ugo+rw " + signerInputDirectory);

        info("calling sign script");
        info("/usr/bin/sign " + adjustToWagonPath(signerInputDirectory) + File.separator + FileUtils.filename(inputFile) + " nomail signed");

        Streams s = exec.executeCommand("/usr/bin/sign " + adjustToWagonPath(signerInputDirectory) + File.separator + FileUtils.filename(inputFile)
                + " nomail signed",false);

        info("sign script output\n" + s.getOut());

        boolean keepChecking = true;
        int count = 0;
        while (keepChecking && count < maxZipChecks)
        {
            try
            {
                info("monitoring for signed file");
                ++count;
                Thread.sleep(zipCheckInterval);

                @SuppressWarnings("unchecked")
                List<String> signedDirList = exec.getFileList(signerOutputDirectory);

                info("signed dir output:");

                for (String line : signedDirList)
                {
                    getLog().info("-> " + line);
                }

                if (signedDirList.size() == 1) // . .. signed file
                {
                    // if ( FileUtils.filename(toSignZipFile).equals(signedDirList.get(0)))
                    // {
                    info("found the signed file");
                    keepChecking = false;
                    // }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if (count == maxZipChecks)
        {
            getLog().error("signer process never signaled completion");
            cleanup(exec,signerInputDirectory);
            return;
        }
        else
        {
            getLog().info("copying signed artifact to :" + outputFile);
            wagon.get(signerOutputDirectory + File.separator + FileUtils.filename(inputFile),new File(outputFile));
            cleanup(exec,signerInputDirectory);
            return;
        }

    }

    private void cleanup(CommandExecutor exec, String signerInputDirectory) throws Exception
    {
        exec.executeCommand("/bin/rm -Rf " + adjustToWagonPath(signerInputDirectory));
        // FileUtils.removePath(signerInputDirectory);
        // FileUtils.removePath(signerOutputDirectory);
    }

    private void info(String log)
    {
        getLog().info("[SIGN] " + log);
    }
}
