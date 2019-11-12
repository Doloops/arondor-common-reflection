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
package com.arondor.common.reflection.bean.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleConstructor;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;

public class AccessibleClassBean implements AccessibleClass
{
    /**
     * 
     */
    private static final long serialVersionUID = 5205325249459644854L;

    private Map<String, AccessibleField> accessibleFields;

    private Map<String, List<String>> accessibleEnums;

    public AccessibleClassBean()
    {

    }

    public void setAccessibleFields(Map<String, AccessibleField> accessibleFields)
    {
        this.accessibleFields = accessibleFields;
    }

    @Override
    public Map<String, AccessibleField> getAccessibleFields()
    {
        return accessibleFields;
    }

    private String name;

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return name;
    }

    private String description;

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    private String longDescription;

    @Override
    public String getLongDescription()
    {
        return longDescription;
    }

    public void setLongDescription(String longDescription)
    {
        this.longDescription = longDescription;
    }

    private String defaultBehavior;

    @Override
    public String getDefaultBehavior()
    {
        return defaultBehavior;
    }

    public void setDefaultBehavior(String defaultBehavior)
    {
        this.defaultBehavior = defaultBehavior;
    }

    @Override
    public String getClassBaseName()
    {
        int idx = name.lastIndexOf('.');
        if (idx != -1)
        {
            return name.substring(idx + 1);
        }
        return name;
    }

    @Override
    public String getPackageName()
    {
        int idx = name.lastIndexOf('.');
        if (idx != -1)
        {
            return name.substring(0, idx);
        }
        return "";
    }

    private List<String> allInterfaces;

    private List<String> interfaces;

    private String superclass;

    @Override
    public List<String> getAllInterfaces()
    {
        return allInterfaces;
    }

    public void setAllInterfaces(List<String> allInterfaces)
    {
        this.allInterfaces = allInterfaces;
    }

    @Override
    public List<String> getInterfaces()
    {
        return interfaces;
    }

    public void setInterfaces(List<String> interfaces)
    {
        this.interfaces = interfaces;
    }

    @Override
    public String getSuperclass()
    {
        return superclass;
    }

    public void setSuperclass(String superclass)
    {
        this.superclass = superclass;
    }

    private List<AccessibleConstructor> constructors;

    @Override
    public List<AccessibleConstructor> getConstructors()
    {
        return constructors;
    }

    public void setConstructors(List<AccessibleConstructor> constructors)
    {
        this.constructors = constructors;
    }

    private List<AccessibleMethod> methods;

    @Override
    public List<AccessibleMethod> getAccessibleMethods()
    {
        return methods;
    }

    public void setAccessibleMethods(List<AccessibleMethod> methods)
    {
        this.methods = methods;
    }

    private boolean abstactClass = false;

    @Override
    public boolean isAbstract()
    {
        return abstactClass;
    }

    public void setAbstract(boolean abstractClass)
    {
        this.abstactClass = abstractClass;
    }

    @Override
    public Map<String, List<String>> getAccessibleEnums()
    {
        return accessibleEnums;
    }

    public void setAccessibleEnums(Map<String, List<String>> accessibleEnums)
    {
        this.accessibleEnums = accessibleEnums;
    }

    public boolean containsEnum(String enumName)
    {

        if (accessibleEnums == null)
        {
            return false;
        }
        return accessibleEnums.containsKey(enumName);

    }

    public void putAccessibleEnum(String enumName, List<String> values)
    {

        if (accessibleEnums == null)
        {
            accessibleEnums = new HashMap<String, List<String>>();
        }
        accessibleEnums.put(enumName, values);

    }

}
