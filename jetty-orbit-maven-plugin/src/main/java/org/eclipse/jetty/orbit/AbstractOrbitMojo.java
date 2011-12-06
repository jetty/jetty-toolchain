package org.eclipse.jetty.orbit;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;

public abstract class AbstractOrbitMojo extends AbstractMojo
{
    /**
     * @parameter default-value="${project.artifactId}"
     * @required
     */
    protected String artifactId;

    /**
     * @parameter default-value="${project.version}"
     * @required
     */
    protected String version;

    /**
     * @parameter default-value="${basedir}"
     * @required
     */
    protected String basedir;

    /**
     * @parameter expression="${project}"
     */
    protected MavenProject project;

    /**
     * Maven ProjectHelper.
     * 
     * @component
     * @readonly
     */
    protected MavenProjectHelper projectHelper;

    /**
     * @parameter expression="${basedir}/src/main/artifacts/${project.artifactId}_${project.version}.jar"
     */
    protected String originalArtifact;
    
    /**
     * @parameter expression="${basedir}/src/main/artifacts/${project.artifactId}.source_${project.version}.jar"
     */
    protected String originalSourceArtifact;
    
    /**
     * @parameter expression="${project.artifactId}_${project.version}.jar"
     */
    protected String orbitFileName;
    
    /**
     * @parameter expression="${project.artifactId}.source_${project.version}.jar"
     */
    protected String orbitSourceFileName;
    
    /**
     * @parameter expression="${project.artifactId}-${project.version}.jar"
     */
    protected String mavenFileName;
    
    /**
     * @parameter expression="${project.artifactId}-${project.version}-sources.jar"
     */
    protected String mavenSourceFileName;
    
    /**
     * @parameter expression="${project.artifactId}-${project.version}-javadoc.jar"
     */
    protected String mavenJavadocFileName;
    
    protected void info(String log)
    {
        getLog().info("[" + this.getClass().getSimpleName() + "] " + log);
    }

}
