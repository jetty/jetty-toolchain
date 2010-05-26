package org.eclipse.jetty.toolchain.enforcer.rules;

//========================================================================
//Copyright (c) Webtide LLC
//------------------------------------------------------------------------
//All rights reserved. This program and the accompanying materials
//are made available under the terms of the Eclipse Public License v1.0
//and Apache License v2.0 which accompanies this distribution.
//
//The Eclipse Public License is available at
//http://www.eclipse.org/legal/epl-v10.html
//
//The Apache License v2.0 is available at
//http://www.apache.org/licenses/LICENSE-2.0.txt
//
//You may elect to redistribute this code under either of these licenses.
//========================================================================

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.junit.Test;

public class RequireRedhatCompatibleVersionRuleTest
{
    @Test
    public void testValidRedhatVersions() throws EnforcerRuleException
    {
        RequireRedhatCompatibleVersionRule rule = new RequireRedhatCompatibleVersionRule();
        // The most basic
        rule.ensureValidRedhatVersion("1.0.0");
        rule.ensureValidRedhatVersion("7.0.2.RC0");
    }

    @Test
    public void testSkipSnapshotRedhatVersions() throws EnforcerRuleException
    {
        RequireRedhatCompatibleVersionRule rule = new RequireRedhatCompatibleVersionRule();
        rule.ensureValidRedhatVersion("1.0.0-SNAPSHOT");
        rule.ensureValidRedhatVersion("7.0.2.SNAPSHOT");
    }

    @Test(expected = EnforcerRuleException.class)
    public void testBadRedhatVersionDash() throws EnforcerRuleException
    {
        RequireRedhatCompatibleVersionRule rule = new RequireRedhatCompatibleVersionRule();
        rule.ensureValidRedhatVersion("7.0.2-RC0");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testBadRedhatVersionReservedAlpha() throws EnforcerRuleException
    {
        RequireRedhatCompatibleVersionRule rule = new RequireRedhatCompatibleVersionRule();
        rule.ensureValidRedhatVersion("7.1.0.alpha3");
    }
}
