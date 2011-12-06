package org.eclipse.jetty.orbit;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;

/**
 * @goal prepare-javadoc
 */
public class PrepareJavadocMojo extends AbstractOrbitMojo
{

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        info("unpacking source artifact for javadoc");
        ZipUnArchiver unzipper = new ZipUnArchiver(new File(originalSourceArtifact));
        String srcDir = basedir + "/src/main/java";
        unzipper.extract("",new File(srcDir));
    }

}
