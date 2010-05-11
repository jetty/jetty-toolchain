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
import org.codehaus.plexus.util.StringUtils;

public class RequireOsgiCompatibleVersionRule implements EnforcerRule
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
            ensureValidOsgiVersion(version);
        }
        catch (ExpressionEvaluationException e)
        {
            throw new EnforcerRuleException("Unable to lookup an expression " + e.getLocalizedMessage(),e);
        }
    }

    public void ensureValidOsgiVersion(String version) throws EnforcerRuleException
    {
        String parts[] = version.split("\\.");
        if (parts.length > 4)
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the OSGi version requirements.  "
                    + "It can't have more than 4 parts (#.#.#.*)" + " - The '.' character has a special meaning");
        }

        if (parts.length < 3)
        {
            throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the OSGi version requirements.  "
                    + "It must have 3 (or 4) parts (#.#.#.*)");
        }

        for (int i = 0; i < 3; i++)
        {
            if (!isNumber(parts[i]))
            {
                throw new EnforcerRuleException("The version \"" + version + "\" does not conform to the OSGi version requirements.  " + "Part #" + (i + 1)
                        + " \"" + parts[i] + "\" of an OSGi version must be a number (#.#.#.*).");
            }
        }
    }

    private boolean isNumber(String str)
    {
        return StringUtils.isNumeric(str);
    }

    public String getCacheId()
    {
        return "osgi-version";
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
