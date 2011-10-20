package org.eclipse.jetty.toolchain.version;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.jetty.toolchain.version.git.GitCommand;

/**
 * Update the active version entry in the VERSION.txt file from information present in the git logs.
 * 
 * @goal update-version-text
 * @requiresProject true
 * @phase package
 */
public class UpdateVersionTextMojo extends AbstractVersionMojo
{
    /**
     * The maven project version.
     * 
     * @parameter expression="${version.section}" default-value="${project.version}"
     * @required
     */
    private String version;

    /**
     * The version key to use in the VERSION.txt file.
     * 
     * @parameter expression="${version.text.key}" default-value="jetty-VERSION"
     * @required
     */
    private String versionTextKey;

    /**
     * The version key to use when looking up a git tag ref.
     * 
     * @parameter expression="${version.tag.key}" default-value="jetty-VERSION"
     * @required
     */
    private String versionTagKey;

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
     * Allow the plugin to update the release date for an issue (if none is provided)
     * 
     * @parameter expression="${version.update.date}" default-value="false"
     */
    private boolean updateDate = false;

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
     * The generated VERSION.txt file.
     * <p>
     * 
     * @parameter expression="${version.text.output.file}" default-value="${project.build.directory}/VERSION.txt"
     */
    private File versionTextOuputFile;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (!hasVersionTextFile("gen-version-text"))
        {
            return; // skip
        }

        try
        {
            String commitMessage = "Updating VERSION.txt";

            // Pattern used in VERSION.txt
            VersionPattern verTextPattern = new VersionPattern(versionTextKey);
            // Pattern used in Git Tags
            VersionPattern verTagPattern = new VersionPattern(versionTagKey);

            VersionText versionText = new VersionText(verTextPattern);
            versionText.read(versionTextInputFile);
            versionText.setSortExisting(sortExisting);

            String updateVersionText = verTextPattern.toVersionId(version);
            String updateVersionGit = verTagPattern.toVersionId(version);
            getLog().debug("raw version = " + version);
            getLog().debug("updateVersionText (as it appears in VERSION.txt) = " + updateVersionText);
            getLog().debug("updateVersionGit (as it appears to git tags) = " + updateVersionGit);

            Release rel = versionText.findRelease(updateVersionText);
            if (rel == null)
            {
                // Not found, create a new one
                rel = new Release(updateVersionText);
                getLog().debug("Not Found, creating new rel = " + rel);
                commitMessage = "Creating new version " + updateVersionText + " in VERSION.txt";
            }
            else
            {
                getLog().debug("Using existing rel = " + rel);
                commitMessage = "Updating version " + updateVersionText + " in VERSION.txt";
            }

            getLog().info("Updating version section: " + version);
            String priorTextVersion = versionText.getPriorVersion(updateVersionText);
            if (priorTextVersion == null)
            {
                // Assume its the top of the file.
                priorTextVersion = versionText.getReleases().get(0).getVersion();
            }
            getLog().debug("Prior version in VERSION.txt is " + priorTextVersion);

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

            // Make sure its an expected version identifier
            if (!verTextPattern.isMatch(priorTextVersion))
            {
                StringBuilder err = new StringBuilder();
                err.append("Prior version [").append(priorTextVersion);
                err.append("] is not a valid version identifier.");
                err.append(" Does not conform to expected pattern [");
                err.append(versionTextKey).append("]");
                throw new MojoExecutionException(err.toString());
            }

            // Make it conform to git tag version identifiers
            String priorGitVersion = verTextPattern.getLastVersion(versionTagKey);
            String priorTagId = git.findTagMatching(priorGitVersion);
            if (priorTagId == null)
            {
                getLog().warn("Unable to find git tag id for prior version id [" + priorGitVersion + "] (defined in VERSION.txt as [" + priorTextVersion + "])");
                getLog().info("Adding empty version section to top for version id [" + updateVersionText + "]");
                versionText.replaceOrPrepend(rel);
                generateVersion(versionText);
                return;
            }
            getLog().debug("Tag for prior version [" + priorGitVersion + "] is " + priorTagId);

            String priorCommitId = git.getTagCommitId(priorTagId);
            getLog().debug("Commit ID from [" + priorTagId + "]: " + priorCommitId);

            String currentCommitId = "HEAD";
            if (refreshTags)
            {
                String currentTagId = git.findTagMatching(updateVersionText);
                if (currentTagId != null)
                {
                    currentCommitId = git.getTagCommitId(currentTagId);
                }
            }
            getLog().debug("Commit ID to [" + updateVersionText + "]: " + currentCommitId);

            git.populateIssuesForRange(priorCommitId,currentCommitId,rel);
            if ((rel.getReleasedOn() == null) && updateDate)
            {
                rel.setReleasedOn(new Date()); // now
            }
            versionText.replaceOrPrepend(rel);

            generateVersion(versionText);

            getLog().info("Update complete. Here's your git command. (Copy/Paste)\ngit commit -m \"" + commitMessage + "\" " + versionTextInputFile.getName());
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
}
