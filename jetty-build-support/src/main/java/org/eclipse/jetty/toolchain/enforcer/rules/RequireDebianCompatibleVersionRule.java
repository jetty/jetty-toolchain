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

import org.apache.maven.enforcer.rule.api.EnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluationException;

public class RequireDebianCompatibleVersionRule implements EnforcerRule
{

    public void execute(EnforcerRuleHelper helper) throws EnforcerRuleException
    {
        try
        {
            String packaging = (String)helper.evaluate("${project.packaging}");
            if ("pom".equals(packaging))
            {
                // Skip pom packaging (not a deployed artifact)
                return;
            }
            String version = (String)helper.evaluate("${project.version}");
            ensureValidDebianVersion(version);
        }
        catch (ExpressionEvaluationException e)
        {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(),e);
        }
    }

    public void ensureValidDebianVersion(String version) throws EnforcerRuleException
    {
        if (version.endsWith("SNAPSHOT"))
        {
            // Skip check on SNAPSHOT versions.
            return;
        }

        if (version.contains("-"))
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Debian (linux packaging) version requirements.  "
                    + "It can't have the '-' character as it has a special meaning reserved for the DEB process (package build #).");
        }

        if (version.contains("_"))
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Debian (linux packaging) version requirements.  "
                    + "It can't have the '_' character as it has a special meaning reserved for the DEB process "
                    + "(indicates split between package name, version, and architecture on filename).");
        }

        // Architecture IDs 
        String reserved[] =
        { "all", "i386", "i486", "i586", "i686", "pentium", "athlon", "ia64", "x86_64", "amd64", "ia32", "alpha", "sparc", "m68k", "ppc", "hppa", "arm" };

        String lversion = version.toLowerCase();
        for (String id : reserved)
        {
            if (lversion.contains(id))
            {
                throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Debian (linux packaging) version requirements.  "
                        + "It can't use the DEB reserved word \"" + id + "\".");
            }
        }
    }

    public String getCacheId()
    {
        return "debian-version";
    }

    public boolean isCacheable()
    {
        return false;
    }

    public boolean isResultValid(EnforcerRule rule)
    {
        return true;
    }

}
