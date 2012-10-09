//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.toolchain.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indication for test cases that require extra knowledge to setup the testing 
 * environment properly.  (Such as more memory required than usual, or a
 * calm system to run large I/O tests on)
 * <p>
 * Tests that have been marked as Stress can often be moved to Slow as time progresses
 * (and general system capabilities improve).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Stress
{
    /**
     * The non-optional reason why the test is set as Stress.
     * <p>
     * Indicate what sort of environmental concerns this test has.
     * <p>
     * Eg: "High memory use: > 2GB", "High file descriptor use", "Needs calm system"
     */
    String value(); 
}
