//
// ========================================================================
// Copyright (c) 1995 Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.toolchain.test.matchers;

import java.util.regex.Pattern;

import org.hamcrest.Description;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

/**
 * @deprecated use {@link Matchers#matchesRegex(String)} instead
 */
@Deprecated
public class RegexMatcher extends TypeSafeMatcher
{
    private final Pattern pattern;

    public RegexMatcher(String pattern)
    {
        this(Pattern.compile(pattern));
    }

    public RegexMatcher(Pattern pattern)
    {
        this.pattern = pattern;
    }

    @Override
    public void describeTo(Description description)
    {
        description.appendText("matches regular expression ").appendValue(pattern);
    }

    @Override
    protected boolean matchesSafely(Object item)
    {
        if (item == null)
            return false;
        return pattern.matcher(item.toString()).matches();
    }
}
