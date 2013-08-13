package com.arondor.common.reflection.model.config;

import java.util.List;
import java.util.Map;

public interface ObjectConfiguration extends ElementConfiguration
{
    String getClassName();

    void setClassName(String className);

    List<ElementConfiguration> getConstructorArguments();

    void setConstructorArguments(List<ElementConfiguration> constructorArguments);

    String getObjectName();

    void setObjectName(String objectName);

    Map<String, ElementConfiguration> getFields();

    void setFields(Map<String, ElementConfiguration> fields);

    void setSingleton(boolean singleton);

    boolean isSingleton();
}
