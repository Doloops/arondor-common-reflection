package com.arondor.common.reflection.model.config;

import java.io.Serializable;
import java.util.List;

public interface FieldConfiguration extends Serializable
{
    public enum FieldConfigurationType
    {
        Primitive_Single, Primitive_Multiple, Object_Single, Object_Multiple
    }

    public void setFieldConfigurationType(FieldConfigurationType objectSingle);

    public FieldConfigurationType getFieldConfigurationType();

    public String getValue();

    public void setValue(String value);

    public List<String> getValues();

    public ObjectConfiguration getObjectConfiguration();

    public List<FieldConfiguration> getObjectConfigurations();

    public void setValues(List<String> arrayList);

    public void setObjectConfigurations(List<FieldConfiguration> objectConfigurations);

    public void setObjectConfiguration(ObjectConfiguration parseBeanDefinition);

}
