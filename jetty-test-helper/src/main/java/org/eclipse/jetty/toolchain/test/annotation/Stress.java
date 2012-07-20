package org.eclipse.jetty.toolchain.test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Stress
{
    /**
     * The optional reason why the test is set as Stress.
     */
    String value(); 
}
