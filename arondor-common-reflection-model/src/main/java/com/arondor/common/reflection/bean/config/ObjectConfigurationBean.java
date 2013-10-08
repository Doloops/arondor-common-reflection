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

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;

@Entity
// @Table(name = "ObjectConfiguration")
@DiscriminatorValue("OBJ")
public class ObjectConfigurationBean implements ObjectConfiguration
{

    /**
     * 
     */
    private static final long serialVersionUID = 5011548715444426003L;

    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.Object;
    }

    public ObjectConfigurationBean()
    {

    }

    @Id
    @GeneratedValue
    private long persistentId;

    public void setPersistentId(long persistentId)
    {
        this.persistentId = persistentId;
    }

    public long getPersistentId()
    {
        return persistentId;
    }

    private String className;

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    @OneToMany(cascade = CascadeType.ALL, targetEntity = ElementConfigurationBean.class)
    // @JoinTable(name = "FieldConfigMap", joinColumns = @JoinColumn(name =
    // "objectConfigId"), inverseJoinColumns = @JoinColumn(name =
    // "fieldConfigId"))
    private Map<String, ElementConfiguration> fields;

    public Map<String, ElementConfiguration> getFields()
    {
        return fields;
    }

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

    @OneToMany(cascade = CascadeType.ALL, targetEntity = ElementConfigurationBean.class)
    private List<ElementConfiguration> constructorArguments;

    public List<ElementConfiguration> getConstructorArguments()
    {
        return constructorArguments;
    }

    public void setConstructorArguments(List<ElementConfiguration> constructorArguments)
    {
        this.constructorArguments = constructorArguments;
    }

    private boolean singleton = false;

    public void setSingleton(boolean singleton)
    {
        this.singleton = singleton;
    }

    public boolean isSingleton()
    {
        return singleton;
    }

    private String objectName;

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(String objectName)
    {
        this.objectName = objectName;
    }

    @Override
    public String toString()
    {
        return "ObjectConfigurationBean [className="
                + className
                + ((constructorArguments != null && !constructorArguments.isEmpty()) ? (", constructor=" + constructorArguments)
                        : "") + ", fields=" + fields + "]";
    }
}
