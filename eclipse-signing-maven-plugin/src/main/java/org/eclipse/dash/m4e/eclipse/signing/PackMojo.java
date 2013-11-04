package org.eclipse.dash.m4e.eclipse.signing;

//========================================================================
//Copyright (c) 2010 Intalio, Inc.
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.UUID;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * 
 * @goal pack
 * @phase package
 * @description runs the eclipse packing process
 */
public class PackMojo extends AbstractEclipseSigningMojo
{
    private static final String __PACK_JAR = "org.eclipse.equinox.p2.jarprocessor_1.0.200.v20100503a.jar";

    private static final String __PACK_PROPERTIES = "pack.properties";

    /**
     * zip file to be packed
     * 
     * @parameter default-value= "${project.build.directory}/site_assembly.zip"
     * @required
     */
    protected String inputFile;

    /**
     * directory monitor for artifact
     * 
     * @parameter default-value="${project.build.directory}/packed"
     * @required
     */
    protected String packedOutputDirectory;

    /**
     * directory for required bits
     * 
     * @parameter default-value="${project.build.directory}/pack-stage"
     * @required
     */
    protected String stagingDirectory;

    /**
     * location of pack200
     * 
     * @parameter default-value="/shared/common/jdk-1.5.0-22.x86_64/bin"
     * @required
     */
    protected String pack200;

    /**
     * @parameter default-value="/usr/local/bin/java"
     * @required
     */
    protected String javaExecutable;

    /** @parameter expression="${project}" */
    protected MavenProject project;

    /**
     * List of patterns for a directory scanner to select the jars that must not be packed.
     * 
     * @parameter
     */
    protected String packExclude;

    /**
     * when true, remove the pack.gz files to make sure no confusion will happen later.
     * 
     * @parameter default-value="false"
     * @required
     */
    protected boolean conditionOnly;

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!FileUtils.fileExists(inputFile))
        {
            throw new MojoFailureException("file to pack does not exist! -> " + inputFile);
        }

        if (!FileUtils.fileExists(packedOutputDirectory))
        {
            info("creating directory to hold packed output");
            FileUtils.mkdir(packedOutputDirectory);
        }

        if (!FileUtils.fileExists(stagingDirectory))
        {
            info("creating directory to hold pack staging bits");
            FileUtils.mkdir(stagingDirectory);
        }

        if (new File(inputFile).isDirectory())
        {

            setupPackProperties(new File(inputFile),null);

            ZipArchiver zipper = new ZipArchiver();
            File destFile = new File(project.getBuild().getDirectory(),"repository-zipped/" + project.getArtifactId() + "-" + project.getVersion() + ".zip");

            try
            {
                destFile.getParentFile().mkdirs();
                zipper.addDirectory(new File(inputFile));
                zipper.setDestFile(destFile);
                zipper.createArchive();
            }
            catch (Exception e)
            {
                throw new MojoExecutionException("Error packing product",e);
            }
            inputFile = destFile.getAbsolutePath();
        }

        try
        {
            packLocally();
        }
        catch (Exception e)
        {
            throw new MojoExecutionException(e.getMessage(),e);
        }

    }

    /**
     * Some jars must not go through the packer. This is controlled via the pack.properties file at the root of the directory where jars are packed.
     * <p>
     * When the pack.properties file is copied, update the property pack.excludes for example:<br/>
     * pack.excludes=plugins/com.ibm.icu.base_3.6.1.v20070417.jar,plugins/com. ibm.icu_3.6.1.v20070417.jar,plugins/com.jcraft.jsch_0.1.31.jar
     * </p>
     * <p>
     * More doc here: https://bugs.eclipse.org/bugs/show_bug.cgi?id=178723
     * </p>
     */
    @SuppressWarnings("deprecation")
    private void setupPackProperties(File targetRepo, File packPropertiesSource) throws MojoExecutionException
    {
        // pack.excludes=com.ibm.icu.base_3.6.1.v20070417.jar,com.ibm.icu_3.6.1.v20070417.jar,com.jcraft.jsch_0.1.31.jar
        Properties props = new Properties();
        InputStream inStream = null;
        OutputStream outStream = null;
        try
        {
            if (packPropertiesSource != null && packPropertiesSource.canRead())
            {
                props.load(inStream);
            }
            else
            {
                // put the default property:
                props.put("pack200.default.args","-E4");
            }
            props.put("pack.excludes",getPackExcluded(targetRepo).toString());

            File out = new File(targetRepo,__PACK_PROPERTIES);
            if (!out.exists())
                out.createNewFile();
            outStream = new FileOutputStream(out);
            props.save(outStream,"Pack.properties generated by the packmojo");
        }
        catch (IOException ioe)
        {
            throw new MojoExecutionException("Unable to setup the pack.properties file",ioe);
        }
        finally
        {
            if (inStream != null)
            {
                try
                {
                    inStream.close();
                }
                catch (IOException ioe)
                {
                }
            }
            if (outStream != null)
            {
                try
                {
                    outStream.close();
                }
                catch (IOException ioe)
                {
                }
            }
        }
    }

    private String getPackExcluded(File targetRepoDirectory)
    {
        StringBuilder patterns = new StringBuilder("artifacts.jar,content.jar");
        if (packExclude != null)
        {
            patterns.append("," + packExclude);
        }
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(targetRepoDirectory);
        StringTokenizer tokenizer = new StringTokenizer(patterns.toString(),", \r\n\t",false);
        String[] incls = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens())
        {
            String tok = tokenizer.nextToken();
            incls[i] = tok;
            i++;
        }
        scanner.setIncludes(incls);
        scanner.scan();
        String[] includedFiles = scanner.getIncludedFiles();
        StringBuilder excluded = null;
        for (String incl : includedFiles)
        {
            incl = incl.replace('\\','/');
            if (excluded == null)
            {
                excluded = new StringBuilder(incl);
            }
            else
            {
                excluded.append("," + incl);
            }
        }
        return excluded.toString();
    }


    private String[] generatePackExecuteString(String jarPackerLoc, String toPack, String outputDirectory)
    {

        // http://wiki.eclipse.org/index.php/Pack200
        /*
         * 3.5 style: java -cp org.eclipse.equinox.p2.jarprocessor_1.0.100.v20090520-1905.jar org.eclipse.equinox.internal.p2.jarprocessor.Main -processAll
         * -repack -sign signing-script.sh -outputDir ./out eclipse-SDK.zip 3.6 syle: java -jar org.eclipse.equinox.p2.jarprocessor_1.0.200.v20100123-1019.jar
         * -processAll -repack -sign signing-script.sh -pack -outputDir ./out eclipse-SDK.zip
         */
        return new String[]
        { "-Dorg.eclipse.update.jarprocessor.pack200=" + pack200, "-jar", jarPackerLoc, "-processAll", "-pack", "-repack",
                // this does not work with the new version of the jar processor
                /*
                 * "-f", FileUtils.dirname(jarPackerLoc) + File.separator + "pack.properties",
                 */
                "-verbose", "-outputDir", outputDirectory, toPack };

    }

    private void positionPackJarForExecution() throws MojoExecutionException
    {
        info("getting jar packer into position");

        URL jarPacker = this.getClass().getClassLoader().getResource(__PACK_JAR);
        URL jarProperties = this.getClass().getClassLoader().getResource(__PACK_PROPERTIES);
        try
        {
            FileUtils.copyURLToFile(jarPacker,new File(stagingDirectory + File.separator + __PACK_JAR));
            FileUtils.copyURLToFile(jarProperties,new File(stagingDirectory + File.separator + __PACK_PROPERTIES));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new MojoExecutionException("unable to position jar packer",e);
        }
    }

    private void packLocally() throws Exception
    {

        if (!FileUtils.fileExists(packedOutputDirectory))
        {
            info("creating directory to hold packed output");
            FileUtils.mkdir(packedOutputDirectory);
        }

        try
        {
            String packTempDirectory = stagingDirectory + File.separator + UUID.randomUUID().toString();

            FileUtils.mkdir(packTempDirectory);

            FileUtils.copyFileToDirectory(inputFile,packTempDirectory);

            CommandLineUtils.StringStreamConsumer out = new CommandLineUtils.StringStreamConsumer();
            CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();

            positionPackJarForExecution();

            Commandline pack = new Commandline();
            pack.setWorkingDirectory(FileUtils.dirname(packTempDirectory));
            pack.setExecutable(javaExecutable);
            pack.addArguments(generatePackExecuteString(stagingDirectory + "/" + __PACK_JAR,inputFile,packedOutputDirectory));

            info("executing pack200, output at end of execution : " + pack);
            CommandLineUtils.executeCommandLine(pack,out,err);

            String packErrput = IOUtil.toString(new StringReader(err.getOutput()));
            String packOutput = IOUtil.toString(new StringReader(out.getOutput()));

            info("pack200 output:\n" + packOutput);
            if (packErrput != null || !"".equals(packErrput))
            {
                getLog().error(packErrput);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            getLog().error(e);
        }
    }

    private void info(String log)
    {
        getLog().info("[PACK] " + log);
    }

}
