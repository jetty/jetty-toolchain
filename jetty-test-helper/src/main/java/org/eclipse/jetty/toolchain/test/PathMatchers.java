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

package org.eclipse.jetty.toolchain.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class PathMatchers
{
    private static class PathPredicate extends TypeSafeMatcher<Path>
    {
        private final Predicate<Path> pathPredicate;
        private final String expectationText;
        private Function<Path, String> describeFailure;

        private PathPredicate(Predicate<Path> pathPredicate, String expectationText)
        {
            this(pathPredicate, expectationText, null);
        }

        private PathPredicate(Predicate<Path> pathPredicate, String expectationText, Function<Path, String> describeFailure)
        {
            this.pathPredicate = Objects.requireNonNull(pathPredicate);
            this.expectationText = Objects.requireNonNull(expectationText);
            this.describeFailure = describeFailure;
        }

        @Override
        public void describeTo(Description description)
        {
            // The "Expected: " line
            description.appendText(expectationText);
        }

        @Override
        protected boolean matchesSafely(Path item)
        {
            return pathPredicate.test(item);
        }

        @Override
        protected void describeMismatchSafely(Path item, Description mismatchDescription)
        {
            // The "but: " line
            mismatchDescription.appendValue(item);
            if (describeFailure != null)
            {
                String butSuffix = describeFailure.apply(item);
                if (butSuffix != null)
                    mismatchDescription.appendText(butSuffix);
            }
        }
    }

    /**
     * Test for {@link Path} that uses {@link Files#exists(Path, LinkOption...)} with clear description of Path if it fails
     *
     * @return the Exists matcher
     */
    public static org.hamcrest.Matcher<Path> exists()
    {
        return new PathPredicate(Files::exists, "path should exist");
    }

    /**
     * Test for {@link Path} that uses {@link Files#isRegularFile(Path, LinkOption...)} with clear description of Path if it fails
     *
     * @return the isRegularFile matcher
     */
    public static org.hamcrest.Matcher<Path> isRegularFile()
    {
        return new PathPredicate(Files::isRegularFile, "path is regular file", (p) ->
        {
            if (!Files.exists(p))
                return " path does not exist";
            if (Files.isDirectory(p))
                return " path is a directory";
            if (Files.isSymbolicLink(p))
                return " path is a symlink";
            return " path type is unknown";
        });
    }

    /**
     * Test for {@link Path} that uses {@link Files#isDirectory(Path, LinkOption...)} with clear description of Path if it fails
     *
     * @return the isDirectory matcher
     */
    public static org.hamcrest.Matcher<Path> isDirectory()
    {
        return new PathPredicate(Files::isDirectory, "path is directory", (p) ->
        {
            if (!Files.exists(p))
                return " path does not exist";
            if (Files.isRegularFile(p))
                return " path is a file";
            if (Files.isSymbolicLink(p))
                return " path is a symlink";
            return " path type is unknown";
        });
    }

    /**
     * Test that {@link Path} exists, as a Directory, and is empty with clear description of Path if it fails
     *
     * @return the isEmptyDirectory matcher
     */
    public static org.hamcrest.Matcher<Path> isEmptyDirectory()
    {
        return new PathPredicate(
            (p) ->
            {
                if (!Files.exists(p))
                    return false;
                if (!Files.isDirectory(p))
                    return false;
                try (Stream<Path> listStream = Files.list(p))
                {
                    long count = listStream.count();
                    return count == 0;
                }
                catch (IOException e)
                {
                    // any error is a fail
                    return false;
                }
            }, "path is directory",
            (p) ->
            {
                if (!Files.exists(p))
                    return " path does not exist";
                if (!Files.isDirectory(p))
                    return " path is not a directory";
                try (Stream<Path> listStream = Files.list(p))
                {
                    long count = listStream.count();
                    return String.format(" path has %,d entries", count);
                }
                catch (IOException e)
                {
                    return String.format(" error reading file list: (%s) %s", e.getClass().getName(), e.getMessage());
                }
            });
    }

    /**
     * Test for {@link Path} that uses {@link Files#isDirectory(Path, LinkOption...)} with clear description of Path if it fails
     *
     * @return the isDirectory matcher
     */
    public static org.hamcrest.Matcher<Path> isSame(final Path expected)
    {
        return new PathPredicate(
            (p) ->
            {
                try
                {
                    return Files.isSameFile(p, expected);
                }
                catch (IOException e)
                {
                    // unable to verify
                    return false;
                }
            }, "path is same",
            (p) ->
                String.format(" is not same as <%s>", expected.toString()));
    }
}
