package com.arondor.common.reflection.model.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ObjectConfiguration extends Serializable
{
    public String getClassName();

    public void setClassName(String className);

    public List<FieldConfiguration> getConstructorArguments();

    public void setConstructorArguments(List<FieldConfiguration> constructorArguments);

    public String getReferenceName();

    public void setReferenceName(String referenceName);

    public Map<String, FieldConfiguration> getFields();

    public void setFields(Map<String, FieldConfiguration> fields);
}
