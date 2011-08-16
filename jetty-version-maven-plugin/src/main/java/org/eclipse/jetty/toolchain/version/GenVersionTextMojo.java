package org.eclipse.jetty.toolchain.version;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.settings.Settings;
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
     * @parameter expression="${version.text.output.file}" default-value="${project.build.directory}/VERSION-gen.txt"
     */
    private File versionTextOuputFile;

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
            getLog().info("Updating version section: " + version);
            String priorVersion = versionText.getPriorVersion(currentVersion);
            if (priorVersion == null)
            {
                throw new MojoFailureException("Unable to find any version prior to " + version);
            }
            getLog().info("Prior version in VERSION.txt is " + priorVersion);

            Release rel = versionText.findRelease(currentVersion);
            if (rel == null)
            {
                // Not found, create a new one
                rel = new Release(currentVersion);
            }

            GitCommand git = new GitCommand();
            git.setWorkDir(basedir);
            git.setLog(getLog());

            String priorTagId = git.findTagMatching(priorVersion);
            String priorCommitId = git.getTagCommitId(priorTagId);
            getLog().info("Commit ID from [" + priorTagId + "]: " + priorCommitId);

            String currentTagId = git.findTagMatching(currentVersion);
            String currentCommitId = "HEAD";
            if (currentTagId != null)
            {
                currentCommitId = git.getTagCommitId(currentTagId);
            }
            getLog().info("Commit ID to [" + currentVersion + "]: " + currentCommitId);

            git.populateIssuesForRange(priorCommitId,currentCommitId,rel);
            if (rel.getReleasedOn() == null)
            {
                rel.setReleasedOn(new Date()); // now
            }
            versionText.replaceOrPrepend(rel);

            versionText.write(versionTextOuputFile);
            getLog().info("New VERSION.txt written at " + versionTextOuputFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            throw new MojoFailureException("Unable to generate replacement VERSION.txt");
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
