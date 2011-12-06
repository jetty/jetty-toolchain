package org.eclipse.jetty.orbit;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal orbitify
 */
public class OrbitifyMojo extends AbstractOrbitMojo
{

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            info("putting artifact into place");
            File targetDirectory = new File(basedir + "/target/");
            
            FileUtils.copyFileToDirectory(originalArtifact,targetDirectory.getAbsolutePath());
            FileUtils.rename(new File(targetDirectory,orbitFileName),new File(targetDirectory,mavenFileName));

            projectHelper.attachArtifact(project,"jar",new File(targetDirectory,mavenFileName));

            info("putting source artifact into place");
            FileUtils.copyFileToDirectory(originalSourceArtifact,targetDirectory.getAbsolutePath());
            FileUtils.rename(new File(targetDirectory,orbitSourceFileName),new File(targetDirectory,mavenSourceFileName));

            projectHelper.attachArtifact(project,"jar","sources",new File(targetDirectory,mavenSourceFileName));

        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
