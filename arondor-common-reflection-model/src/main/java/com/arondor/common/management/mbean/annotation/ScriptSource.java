package com.arondor.common.management.mbean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
/**
 * Determines that a field is a Script source, multi-line field
 * 
 * @author Francois Barre
 *
 */
public @interface ScriptSource
{
    /**
     * Format, syntax of this script
     */
    String value();
}
