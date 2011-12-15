/*******************************************************************************
 * Copyright (c) 2011 Intalio, Inc.
 * ======================================================================
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 *   The Eclipse Public License is available at
 *   http://www.eclipse.org/legal/epl-v10.html
 *
 *   The Apache License v2.0 is available at
 *   http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 *******************************************************************************/
package org.eclipse.jetty.toolchain.version;

import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test version pattern
 */
@RunWith(Parameterized.class)
public class VersionPatternTest
{
    private static final String ECLIPSE_ID = "Eclipse";
    private static final String ECLIPSE_KEY = "jetty-VERSION";
    private static final String CODEHAUS_ID = "Codehaus";
    private static final String CODEHAUS_KEY = "jetty@codehaus-VERSION";

    @Parameters
    public static List<Object[]> data()
    {
        List<Object[]> data = new ArrayList<Object[]>();
        // Eclipse Format
        data.add(new Object[]
        { ECLIPSE_ID, ECLIPSE_KEY, "jetty-7.5.0.RC2", "jetty-7.5.0.RC2" });
        data.add(new Object[]
        { ECLIPSE_ID, ECLIPSE_KEY, "Jetty-6.1.2", "jetty-6.1.2" });
        data.add(new Object[]
        { ECLIPSE_ID, ECLIPSE_KEY, "Jetty 2.0Alpha2", "jetty-2.0Alpha2" });
        // Codehause Format
        data.add(new Object[]
        { CODEHAUS_ID, CODEHAUS_KEY, "jetty@codehaus 7.4.2.v20110526", "jetty@codehaus-7.4.2.v20110526" });
        data.add(new Object[]
        { CODEHAUS_ID, CODEHAUS_KEY, "jetty@codehaus 7.0.0.RC4", "jetty@codehaus-7.0.0.RC4" });
        data.add(new Object[]
        { CODEHAUS_ID, CODEHAUS_KEY, "Jetty@Codehaus 7.4.4.v20110707", "jetty@codehaus-7.4.4.v20110707" });
        return data;
    }

    private String id;
    private String key;
    private String rawVer;
    private String expectedVersion;

    public VersionPatternTest(String id, String key, String rawVer, String expectedVersion)
    {
        this.id = id;
        this.key = key;
        this.rawVer = rawVer;
        this.expectedVersion = expectedVersion;
    }

    @Test
    public void testVersion()
    {
        VersionPattern pat = new VersionPattern(key);
        Assert.assertThat("VersionPattern(" + id + ").isMatch(\"" + rawVer + "\")",pat.isMatch(rawVer),is(true));
        Assert.assertThat("VersionPattern(" + id + ").getLastVersion()",expectedVersion,is(pat.getLastVersion()));

        String expectedAltVersion = expectedVersion.replaceFirst("^.*-","alt-");
        String altKey = "alt-VERSION";
        Assert.assertThat("VersionPattern(" + id + ").getLastVersion(ALTKEY)",expectedAltVersion,is(pat.getLastVersion(altKey)));
    }
}
