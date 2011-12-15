/*******************************************************************************
 * Copyright (c) 2011 Intalio, Inc.
 * ======================================================================
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *   The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *
 *   The Apache License v2.0 is available at
 *   http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/
package org.eclipse.jetty.toolchain.version;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class AbstractVersionMojo extends AbstractMojo
{
    /**
     * The project basedir.
     * 
     * @parameter expression="${project.basedir}"
     * @required
     */
    protected File basedir;
    
    /**
     * The existing VERSION.txt file.
     * <p>
     * 
     * @parameter expression="${version.text.file}" default-value="${project.basedir}/VERSION.txt"
     */
    protected File versionTextInputFile;
    
    /**
     * The classifier to use for attaching the generated VERSION.txt artifact
     * 
     * @parameter expression=${version.text.output.classifier}" default-value="version"
     */
    protected String classifier = "version";
    
    /**
     * The type to use for the attaching the generated VERSION.txt artifact
     * 
     * @parameter expression=${version.text.output.type}" default-value="txt"
     */
    protected String type = "txt";
    
    /**
     * Maven ProjectHelper. (internal component)
     * 
     * @component
     * @readonly
     * @required
     */
    protected MavenProjectHelper projectHelper;
    
    /**
     * Maven Project.
     * 
     * @parameter expression="${project}"
     * @readonly
     * @required
     */
    protected MavenProject project;

    protected void ensureDirectoryExists(File dir) throws MojoFailureException
    {
        if (dir.exists() && dir.isDirectory())
        {
            return; // done
        }
    
        if (dir.mkdirs() == false)
        {
            throw new MojoFailureException("Unable to create directory: " + dir.getAbsolutePath());
        }
    }

    protected boolean hasVersionTextFile(String goal)
    {
        if (versionTextInputFile == null)
        {
            getLog().debug("Skipping :" + goal + " - the <versionTextInputFile> was not specified.");
            return false; // skipping build,
        }
    
        if (!versionTextInputFile.exists())
        {
            getLog().debug("Skipping :" + goal + " - file not found: " + versionTextInputFile.getAbsolutePath());
            return false; // skipping build,
        }
    
        return true;
    }
}
