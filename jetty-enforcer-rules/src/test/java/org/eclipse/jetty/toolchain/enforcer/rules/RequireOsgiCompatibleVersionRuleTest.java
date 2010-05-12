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

public class RequireOsgiCompatibleVersionRuleTest
{
    @Test
    public void testValidOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        // The most basic
        rule.ensureValidOsgiVersion("1.0.0");
        // The second most basic
        rule.ensureValidOsgiVersion("1.2.3.4");
        // A long form classifier
        rule.ensureValidOsgiVersion("12.0.0.v20100511-114423");
        // A SNAPSHOT version
        rule.ensureValidOsgiVersion("4.0.2.SNAPSHOT");
        // Some creative versions found "in the wild"
        rule.ensureValidOsgiVersion("3.4.2.r342_v20081028-0800");
        rule.ensureValidOsgiVersion("3.4.3.R34x_v20081215-1030");
        rule.ensureValidOsgiVersion("3.0.5.v20090218-1800-e3x");
        rule.ensureValidOsgiVersion("1.1.1.M20080827-0800b");
        rule.ensureValidOsgiVersion("0.1.37.v200803061811");
    }

    @Test(expected = EnforcerRuleException.class)
    public void testTooLongOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1.2.3.4.5");
    }

    @Test(expected = EnforcerRuleException.class)
    public void testTooShortOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1.2");
    }

    @Test(expected = EnforcerRuleException.class)
    public void testBadSnapshotOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("7.0.2-SNAPSHOT");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testLettersInPartOneOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("a.0.2");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testLettersInPartOneAOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1a.0.2");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testLettersInPartTwoOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1.1a.2");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testBetaOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1.beta.2");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testAlphaOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1.0.alpha-2");
    }
    
    @Test(expected = EnforcerRuleException.class)
    public void testInvalidQualifierOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("1.0.2.2009:05:12");
    }

    @Test
    public void testGoodSnapshotOsgiVersion() throws EnforcerRuleException
    {
        RequireOsgiCompatibleVersionRule rule = new RequireOsgiCompatibleVersionRule();
        rule.ensureValidOsgiVersion("7.0.2.SNAPSHOT");
    }
}
