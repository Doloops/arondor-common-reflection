package com.arondor.common.management.mbean.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Description
{
    /**
     * The description associated with this element
     * @return the description associated with this element
     */
    String value();
}
