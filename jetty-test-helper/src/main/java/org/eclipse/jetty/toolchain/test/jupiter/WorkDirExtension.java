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

package org.eclipse.jetty.toolchain.test.jupiter;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.eclipse.jetty.toolchain.test.MavenPaths;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.ReflectionUtils;

import static org.junit.platform.commons.util.ReflectionUtils.isPrivate;
import static org.junit.platform.commons.util.ReflectionUtils.makeAccessible;

public class WorkDirExtension implements BeforeAllCallback, BeforeEachCallback, ParameterResolver
{
    @Override
    public void beforeAll(ExtensionContext context)
    {
        injectStaticFields(context, context.getRequiredTestClass());
    }

    @Override
    public void beforeEach(ExtensionContext context)
    {
        context.getRequiredTestInstances().getAllInstances()
            .forEach(instance -> injectInstanceFields(context, instance));
    }

    private void injectStaticFields(ExtensionContext context, Class<?> testClass)
    {
        injectFields(context, null, testClass, ReflectionUtils::isStatic);
    }

    private void injectInstanceFields(ExtensionContext context, Object instance)
    {
        injectFields(context, instance, instance.getClass(), ReflectionUtils::isNotStatic);
    }

    private void injectFields(ExtensionContext context, Object testInstance, Class<?> testClass,
                              Predicate<Field> fieldPredicate)
    {
        if (!context.getTestInstance().isPresent())
            return;

        Path testPath = MavenPaths.targetTestDir(context);
        WorkDir workdir = new WorkDir(testPath);

        Predicate<Field> isWorkDirAssignable = (f)-> WorkDir.class.isAssignableFrom(f.getType());

        ReflectionUtils.findFields(testClass,
            fieldPredicate.and(isWorkDirAssignable),
            ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
            .forEach(field ->
            {
                assertValidFieldCandidate(field);
                try
                {
                    makeAccessible(field).set(testInstance, workdir);
                }
                catch (Throwable t)
                {
                    ExceptionUtils.throwAsUncheckedException(t);
                }
            });
    }

    private void assertValidFieldCandidate(Field field)
    {
        if (isPrivate(field))
        {
            throw new ExtensionConfigurationException("WorkDir field [" + field + "] must not be private.");
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return WorkDir.class.isAssignableFrom(parameterContext.getParameter().getType());
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        Class<?> clazz = parameterContext.getDeclaringExecutable().getDeclaringClass();
        String methodName = null;
        if (extensionContext.getTestInstance().isPresent())
            methodName = extensionContext.getTestMethod().get().getName();
        String displayName = extensionContext.getDisplayName();
        Path dir = MavenPaths.targetTestDir(clazz, methodName, displayName);
        return new WorkDir(dir);
    }
}
