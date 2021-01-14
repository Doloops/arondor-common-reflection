/*
 *  Copyright 2013, Arondor
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.arondor.common.reflection.bean.config;

import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;

public class ObjectConfigurationBean implements ObjectConfiguration
{
    private static final long serialVersionUID = 5011548715444426003L;

    public ObjectConfigurationBean()
    {

    }

    @Override
    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.Object;
    }

    private String className;

    @Override
    public String getClassName()
    {
        return className;
    }

    @Override
    public void setClassName(String className)
    {
        this.className = className;
    }

    private Map<String, ElementConfiguration> fields;

    @Override
    public Map<String, ElementConfiguration> getFields()
    {
        return fields;
    }

    @Override
    public void setFields(Map<String, ElementConfiguration> fields)
    {
        this.fields = fields;
    }

    private String referenceName;

    public String getReferenceName()
    {
        return referenceName;
    }

    public void setReferenceName(String referenceName)
    {
        this.referenceName = referenceName;
    }

    private List<ElementConfiguration> constructorArguments;

    @Override
    public List<ElementConfiguration> getConstructorArguments()
    {
        return constructorArguments;
    }

    @Override
    public void setConstructorArguments(List<ElementConfiguration> constructorArguments)
    {
        this.constructorArguments = constructorArguments;
    }

    private boolean singleton = false;

    @Override
    public void setSingleton(boolean singleton)
    {
        this.singleton = singleton;
    }

    @Override
    public boolean isSingleton()
    {
        return singleton;
    }

    private String objectName;

    @Override
    public String getObjectName()
    {
        return objectName;
    }

    @Override
    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    @Override
    public String toString()
    {
        return "ObjectConfigurationBean [className=" + className
                + ((constructorArguments != null && !constructorArguments.isEmpty())
                        ? (", constructor=" + constructorArguments)
                        : "")
                + ", fields=" + fields + "]";
    }
}
