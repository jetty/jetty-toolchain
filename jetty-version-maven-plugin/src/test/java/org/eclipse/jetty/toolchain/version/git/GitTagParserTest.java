package org.eclipse.jetty.toolchain.version.git;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.junit.Assert;
import org.junit.Test;

public class GitTagParserTest extends AbstractGitTestCase
{
    @Test
    public void testGitTagParse() throws IOException
    {
        File sampleFile = MavenTestingUtils.getTestResourceFile("git-tag.txt");
        GitTagParser parser = new GitTagParser();
        parseSampleFile(parser,sampleFile);

        Assert.assertNotNull("parser.tagIds",parser.getTagIds());
        Assert.assertEquals("parser.tagIds.size",121,parser.getTagIds().size());
    }
}
