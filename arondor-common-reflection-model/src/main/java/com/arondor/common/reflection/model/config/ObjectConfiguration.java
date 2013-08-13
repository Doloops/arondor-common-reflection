package com.arondor.common.reflection.model.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface ObjectConfiguration extends Serializable
{
    String getClassName();

    void setClassName(String className);

    List<FieldConfiguration> getConstructorArguments();

    void setConstructorArguments(List<FieldConfiguration> constructorArguments);

    String getReferenceName();

    void setReferenceName(String referenceName);

    Map<String, FieldConfiguration> getFields();

    void setFields(Map<String, FieldConfiguration> fields);

    void setSingleton(boolean singleton);

    boolean isSingleton();
}
