package com.arondor.common.reflection.model.config;

public interface PrimitiveConfiguration extends ElementConfiguration
{
    public String getValue();

    public void setValue(String value);
}
