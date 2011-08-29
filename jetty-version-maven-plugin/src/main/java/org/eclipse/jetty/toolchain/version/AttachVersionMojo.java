package org.eclipse.jetty.toolchain.version;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Attach the VERSION.txt to the project.
 * <p>
 * Will only attach the VERSION.txt if it exists.
 * 
 * @goal attach-version-text
 * @requiresProject true
 * @phase process-resources
 */
public class AttachVersionMojo extends AbstractVersionMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!hasVersionTextFile("attach-version-text"))
        {
            return; // skip
        }
        
        projectHelper.attachArtifact(project,type,classifier,versionTextInputFile);
    }
}
