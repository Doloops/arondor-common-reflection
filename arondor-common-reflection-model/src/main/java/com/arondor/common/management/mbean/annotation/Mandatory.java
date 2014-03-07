package com.arondor.common.management.mbean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Mandatory
{
	/**
	 * Is mandatory field ?
	 * 
	 * @return
	 */
    boolean isMandatory() default true;
    
}
