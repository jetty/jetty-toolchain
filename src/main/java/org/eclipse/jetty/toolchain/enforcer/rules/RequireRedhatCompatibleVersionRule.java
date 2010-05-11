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

public class RequireRedhatCompatibleVersionRule implements EnforcerRule
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
            ensureValidRedhatVersion(version);
        }
        catch (ExpressionEvaluationException e)
        {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(),e);
        }
    }

    public void ensureValidRedhatVersion(String version) throws EnforcerRuleException
    {
        if (version.endsWith("SNAPSHOT"))
        {
            // Skip check on SNAPSHOT versions.
            return;
        }

        if (version.contains("-"))
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Redhat (linux packaging) version requirements.  "
                    + "It can't have the '-' character as it has a special meaning reserved for the RPM process.");
        }

        String reserved[] =
        {
                // Special IDs 
                "noarch", "nosrc",
                "src",
                // Optimization IDs 
                "fat", "i386", "i486", "i586", "i686", "pentium", "athlon", "ia64", "x86_64", "amd64", "ia32e", "alpha", "sparc", "m68k", "ppc", "parisc",
                "hppa", "mips", "mipsel", "armv", "atari", "falcon", "milan", "hades", "s390",
                // OS IDs
                "aix", "amigaos", "bsd", "cygwin", "darwin", "freebsd", "freemint", "hp", "hpux", "irix", "linux", "machten", "macosx", "mint", "mp_ras",
                "nextstep", "os/390", "osf1", "osf3.2", "osf4.0", "sco_sv", "solaris", "sunos", "unix", "ux", "vm", "esa",
                // Architecture IDs (without overlap from above)
                "sun", "xtensa", "rs6000", "sgi" };

        for (String id : reserved)
        {
            if (version.contains(id))
            {
                throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the Redhat (linux packaging) version requirements.  "
                        + "It can't use the RPM reserved word \"" + id + "\".");
            }
        }
    }

    public String getCacheId()
    {
        return "redhat-version";
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
