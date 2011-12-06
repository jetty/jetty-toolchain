package org.eclipse.jetty.orbit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal validate
 */
public class ValidateMojo extends AbstractOrbitMojo
{

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!FileUtils.fileExists(originalArtifact))
        {
            throw new MojoFailureException("must have source artifact: " + originalArtifact);
        }

        if (!FileUtils.fileExists(originalSourceArtifact))
        {
            throw new MojoFailureException("must have source artifact: " + originalSourceArtifact);
        }

    }

}
