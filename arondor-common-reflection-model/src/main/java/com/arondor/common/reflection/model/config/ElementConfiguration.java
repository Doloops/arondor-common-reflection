package com.arondor.common.reflection.model.config;

import java.io.Serializable;

public interface ElementConfiguration extends Serializable
{
    public enum ElementConfigurationType
    {
        Primitive, Object, Reference, List, Map
    }

    ElementConfigurationType getFieldConfigurationType();
}
