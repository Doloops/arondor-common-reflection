package com.arondor.common.management.mbean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Password
{
    /**
     * Is password field ?
     * 
     * @return true if the annotation is set, false otherwise
     */
    boolean isPassword() default true;

}
