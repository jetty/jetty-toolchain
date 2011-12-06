package org.eclipse.jetty.orbit;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.archiver.zip.ZipArchiver;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal process-javadoc
 */
public class ProcessJavadocMojo extends AbstractOrbitMojo
{

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            info("putting javadoc artifact into place");
            File targetDirectory = new File(basedir + "/target/");
            
            ZipArchiver zipper = new ZipArchiver();
            zipper.addDirectory(new File(targetDirectory, "/site/apidocs"));

            zipper.setDestFile(new File(targetDirectory, mavenJavadocFileName));

            zipper.createArchive();

            projectHelper.attachArtifact(project,"jar","javadoc",new File(targetDirectory, mavenJavadocFileName));

        }
        catch (Exception e)
        {
            throw new MojoExecutionException("mojo failed to execute", e);
        }
    }
}
