//
//  ========================================================================
//  Copyright (c) 1995-2020 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.toolchain.test.jupiter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.StringMangler;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.ExceptionUtils;
import org.junit.platform.commons.util.ReflectionUtils;

import static java.nio.charset.StandardCharsets.UTF_8;
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

        try
        {
            Object obj = context.getTestInstance().get();
            Path testPath = toPath(obj.getClass(), context);
            WorkDir workdir = new WorkDir(testPath);

            Predicate<Field> isWorkDirAssignable = (f)-> f.getType().isAssignableFrom(WorkDir.class);

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
        catch (IOException e)
        {
            throw new RuntimeException("Unable to establish WorkDir path", e);
        }
    }

    private void assertValidFieldCandidate(Field field)
    {
        if (isPrivate(field))
        {
            throw new ExtensionConfigurationException("WorkDir field [" + field + "] must not be private.");
        }
    }

    private Path toPath(Class<?> classContext, ExtensionContext context) throws IOException
    {
        StringBuilder dirName = new StringBuilder();

        Class<?> clazz = context.getTestClass().orElse(classContext);
        dirName.append(StringMangler.condensePackageString(clazz.getName()));
        dirName.append(File.separatorChar);

        if (context.getTestMethod().isPresent())
        {
            String methodname = context.getTestMethod().get().getName();
            if (OS.WINDOWS.isCurrentOs())
            {
                dirName.append(StringMangler.maxStringLength(30, methodname));
            }
            else
            {
                dirName.append(methodname);
            }

            if (!context.getDisplayName().startsWith(methodname))
            {
                dirName.append(URLEncoder.encode(context.getDisplayName().trim(), UTF_8.toString()));
            }
        }
        else
        {
            dirName.append(URLEncoder.encode(context.getDisplayName().trim(), UTF_8.toString()));
        }

        return MavenTestingUtils.getTargetTestingPath().resolve(dirName.toString());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType().isAssignableFrom(WorkDir.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        try
        {
            Path dir = toPath(parameterContext.getDeclaringExecutable().getDeclaringClass(), extensionContext);
            return new WorkDir(dir);
        }
        catch (IOException e)
        {
            throw new ParameterResolutionException("Unable to resolve work dir", e);
        }
    }
}
