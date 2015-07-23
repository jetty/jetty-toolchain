//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
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

package org.eclipse.jetty.toolchain.test;

import org.junit.AssumptionViolatedException;

/**
 * Flag indicating that {@link org.junit.Test} is part of special group of tests.
 */
public final class PropertyFlag
{
    private PropertyFlag()
    {
        /* prevent instantiation */
    }

    /**
     * Returns flag indicating if <code>-D&lt;flag&gt;</code> or <code>-D&lt;flag&gt;=true</code> is enabled.
     * 
     * @param property
     *            name of the system property representing this flag
     * @return true if enabled
     */
    public static boolean isEnabled(String property)
    {
        String stress = System.getProperty(property);
        if (stress == null)
        {
            return false;
        }

        if ("".equals(stress))
        {
            return true;
        }

        return Boolean.parseBoolean(stress);
    }

    /**
     * Junit Assumption based on the value of a System Property.
     * <p>
     * If property is found, with no value, then it assumed to be true. Otherwise the value is parsed a
     * {@link Boolean#parseBoolean(String)} and used for junit assume logic.
     * 
     * @param property
     *            the system property to look for
     */
    public static void assume(String property)
    {
        if (!isEnabled(property))
        {
            throw new AssumptionViolatedException("System Property '" + property + "' not set");
        }
    }
}
