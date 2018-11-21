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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequireDebianCompatibleVersionRuleTest
{
    @Test
    public void testGoodDebianVersions()
        throws EnforcerRuleException
    {
        RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule();
        rule.ensureValidDebianVersion( "1.0.0" );
        rule.ensureValidDebianVersion( "7.0.2.RC0" );
    }

    @Test
    public void testSkippedSnapshotDebianVersions()
        throws EnforcerRuleException
    {
        RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule();
        rule.ensureValidDebianVersion( "1.0.0-SNAPSHOT" );
        rule.ensureValidDebianVersion( "7.0.2.SNAPSHOT" );
    }

    @Test
    public void testBadDebianVersionDash()
        throws EnforcerRuleException
    {
        Assertions.assertThrows( EnforcerRuleException.class, () -> {
            RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule();
            rule.ensureValidDebianVersion( "7.0.2-RC0" );
        } );
    }

    @Test
    public void testBadDebianVersionUnderscore()
        throws EnforcerRuleException
    {
        Assertions.assertThrows( EnforcerRuleException.class, () -> {
            RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule();
            rule.ensureValidDebianVersion( "7.0.2_RC0" );
        } );
    }

    @Test
    public void testBadDebianVersionReservedAlpha()
        throws EnforcerRuleException
    {
        Assertions.assertThrows( EnforcerRuleException.class, () -> {
            RequireDebianCompatibleVersionRule rule = new RequireDebianCompatibleVersionRule();
            rule.ensureValidDebianVersion( "7.0.2.alpha3" );
        } );
    }
}
