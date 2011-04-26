package org.eclipse.jetty.toolchain.test;

import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;

/**
 * Flag indicating that {@link Test} is part of special group of tests.
 */
public class PropertyFlag
{
    /**
     * Returns flag indicating if <code>-D&lt;flag&gt;</code> or <code>-D&lt;flag&gt=true</code> is enabled.
     * 
     * @param property name of the system property representing this flag
     * @return true if enabled
     */
    public static boolean isEnabled(String property)
    {
        String stress = System.getProperty(property);
        if (stress == null)
        {
            return false;
        }

        if (stress.equals(""))
        {
            return true;
        }

        return Boolean.parseBoolean(stress);
    }

    /**
     * Labels the test method as belonging to Stress testing.
     * <p>
     * Checks for the existence of the "STRESS" system property, if found, it allows the test to execute. If not found,
     * the test is flagged as ignored and returned.
     */
    public static void assume(String property)
    {
        if (!isEnabled(property))
        {
            throw new AssumptionViolatedException("Test is not enabled");
        }
    }
}
