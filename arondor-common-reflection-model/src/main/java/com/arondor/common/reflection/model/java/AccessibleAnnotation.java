package com.arondor.common.reflection.model.java;

import java.io.Serializable;

public interface AccessibleAnnotation extends Serializable
{
    String getValue();

    /**
     * TODO : Support more complex annotations
     */
}
