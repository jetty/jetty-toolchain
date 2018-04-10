//
//  ========================================================================
//  Copyright (c) 1995-2018 Mort Bay Consulting Pty. Ltd.
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
import java.nio.file.Path;

import org.eclipse.jetty.toolchain.test.MavenTestingUtils;
import org.eclipse.jetty.toolchain.test.StringMangler;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class WorkDirExtension implements BeforeEachCallback, ParameterResolver
{
    @Override
    public void beforeEach(ExtensionContext context) throws IOException
    {
        if (!context.getTestInstance().isPresent())
            throw new RuntimeException("Unable to use @" + WorkDir.class.getSimpleName() + " on this type of test");

        Object obj = context.getTestInstance().get();

        Path testPath = toPath(obj.getClass(), context);

        for (Field field : obj.getClass().getDeclaredFields())
        {
            if (!field.getType().isAssignableFrom(WorkDir.class))
                continue; // skip

            try
            {
                field.set(obj, new WorkDir(testPath));
            }
            catch (IllegalAccessException iae)
            {
                throw new RuntimeException(iae);
            }
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

            if(!context.getDisplayName().startsWith(methodname))
            {
                dirName.append(context.getDisplayName().trim());
            }
        }
        else
        {
            dirName.append(context.getDisplayName().trim());
        }

        return MavenTestingUtils.getTargetTestingPath().resolve(dirName.toString());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException
    {
        return parameterContext.getParameter().getType() .isAssignableFrom(WorkDir.class);
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
