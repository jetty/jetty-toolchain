package org.eclipse.jetty.toolchain.version;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jetty.toolchain.version.git.GitCommand;

/**
 * Fetch the active version entries from git logs and prepend the VERSION.txt with the entries found.
 * 
 * @goal gen-version-text
 * @requiresProject true
 * @phase package
 */
public class GenVersionTextMojo extends AbstractMojo
{
    /**
     * The project basedir.
     * 
     * @parameter expression="${project.basedir}"
     * @required
     */
    private File basedir;

    /**
     * The current user system settings for use in Maven.
     * 
     * @parameter expression="${settings}"
     * @required
     * @readonly
     */
    protected Settings settings;

    /**
     * The maven project.
     * 
     * @parameter expression="${version.section}" default-value="${project.version}"
     * @required
     */
    private String version;

    /**
     * Allow the existing issues to be sorted alphabetically.
     * 
     * @parameter expression="${version.sort.existing}" default-value="false"
     */
    private boolean sortExisting = false;

    /**
     * Allow the plugin to issue a 'git fetch --tags' to update the local tags from.
     * 
     * @parameter expression="${version.refresh.tags}" default-value="false"
     */
    private boolean refreshTags = false;

    /**
     * Allow the plugin to replace the input VERSION.txt file
     * 
     * @parameter expression="${version.copy.generated}" default-value="false"
     */
    private boolean copyGenerated;

    /**
     * Allow the plugin to attach the generated VERSION.txt file to the project
     * 
     * @parameter expression="${version.attach}" default-value="false"
     */
    private boolean attachArtifact;

    /**
     * The existing VERSION.txt file.
     * <p>
     * 
     * @parameter expression="${version.text.file}" default-value="${project.basedir}/VERSION.txt"
     */
    private File versionTextInputFile;

    /**
     * The generated VERSION.txt file.
     * <p>
     * 
     * @parameter expression="${version.text.output.file}" default-value="${project.build.directory}/VERSION.txt"
     */
    private File versionTextOuputFile;

    /**
     * The classifier to use for attaching the generated VERSION.txt artifact
     * 
     * @parameter expression=${version.text.output.classifier}" default-value="version"
     */
    private String classifier = "version";

    /**
     * The type to use for the attaching the generated VERSION.txt artifact
     * 
     * @parameter expression=${version.text.output.type}" default-value="txt"
     */
    private String type = "txt";

    /**
     * Maven ProjectHelper. (internal component)
     * 
     * @component
     * @readonly
     * @required
     */
    private MavenProjectHelper projectHelper;

    /**
     * Maven Project.
     * 
     * @parameter expression="${project}"
     * @readonly
     * @required
     */
    private MavenProject project;

    private void ensureDirectoryExists(File dir) throws MojoFailureException
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

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!hasVersionTextFile())
        {
            return; // skip
        }

        try
        {
            VersionText versionText = new VersionText();
            versionText.read(versionTextInputFile);
            versionText.setSortExisting(sortExisting);

            String currentVersion = versionText.toFullVersion(version);
            Release rel = versionText.findRelease(currentVersion);
            if (rel == null)
            {
                // Not found, create a new one
                rel = new Release(currentVersion);
            }

            getLog().info("Updating version section: " + version);
            String priorVersion = versionText.getPriorVersion(currentVersion);
            if (priorVersion == null)
            {
                // Assume its the top of the file.
                priorVersion = versionText.getReleases().get(0).getVersion();
            }
            getLog().debug("Prior version in VERSION.txt is " + priorVersion);

            GitCommand git = new GitCommand();
            git.setWorkDir(basedir);
            git.setLog(getLog());

            if (refreshTags)
            {
                getLog().info("Fetching git tags from remote ...");
                if (!git.fetchTags())
                {
                    throw new MojoFailureException("Unable to fetch git tags?");
                }
            }

            String priorTagId = git.findTagMatching(priorVersion);
            if (priorTagId == null)
            {
                getLog().warn("Unable to find git tag id for prior version id [" + priorVersion + "] (defined in VERSION.txt)");
                getLog().info("Adding empty version section to top for version id [" + currentVersion + "]");
                versionText.replaceOrPrepend(rel);
                generateVersion(versionText);
                return;
            }
            getLog().debug("Tag for prior version [" + priorVersion + "] is " + priorTagId);

            String priorCommitId = git.getTagCommitId(priorTagId);
            getLog().debug("Commit ID from [" + priorTagId + "]: " + priorCommitId);

            String currentTagId = git.findTagMatching(currentVersion);
            String currentCommitId = "HEAD";
            if (currentTagId != null)
            {
                currentCommitId = git.getTagCommitId(currentTagId);
            }
            getLog().debug("Commit ID to [" + currentVersion + "]: " + currentCommitId);

            git.populateIssuesForRange(priorCommitId,currentCommitId,rel);
            if (rel.getReleasedOn() == null)
            {
                rel.setReleasedOn(new Date()); // now
            }
            versionText.replaceOrPrepend(rel);

            generateVersion(versionText);
        }
        catch (IOException e)
        {
            throw new MojoFailureException("Unable to generate replacement VERSION.txt",e);
        }
    }

    private void generateVersion(VersionText versionText) throws MojoFailureException, IOException
    {
        ensureDirectoryExists(versionTextOuputFile.getCanonicalFile().getParentFile());
        versionText.write(versionTextOuputFile);
        getLog().debug("New VERSION.txt written at " + versionTextOuputFile.getAbsolutePath());

        if (attachArtifact)
        {
            getLog().info("Attaching generated VERSION.txt");
            getLog().debug("Classifier = " + classifier);
            getLog().debug("Type = " + type);
            projectHelper.attachArtifact(project,type,classifier,versionTextOuputFile);
        }

        if (copyGenerated)
        {
            getLog().info("Copying generated VERSION.txt over input VERSION.txt");
            FileUtils.copyFile(versionTextOuputFile,versionTextInputFile);
        }
    }

    private boolean hasVersionTextFile()
    {
        if (versionTextInputFile == null)
        {
            getLog().info("Skipping :version-text-gen - the <versionTextInputFile> was not specified.");
            return false; // skipping build,
        }

        if (!versionTextInputFile.exists())
        {
            getLog().info("Skipping :version-text-gen - file not found: " + versionTextInputFile.getAbsolutePath());
            return false; // skipping build,
        }

        return true;
    }
}
