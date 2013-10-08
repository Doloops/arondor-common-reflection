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

import java.util.List;

import com.arondor.common.reflection.model.java.AccessibleField;

/**
 * Exposed object attribute information, used in MbeanObject to describe an
 * attribute
 * 
 * @author Francois Barre
 * 
 */
public class AccessibleFieldBean implements AccessibleField
{

    /**
     * 
     */
    private static final long serialVersionUID = -5032370716705853844L;

    public AccessibleFieldBean()
    {

    }

    /**
     * Attribute name
     */
    private String name;

    /**
     * Attribute description
     */
    private String description;

    /**
     * Attribute class
     */
    private String className;

    /**
     * Is this attribute readable : does it have a getter
     */
    private boolean readable;

    /**
     * Is this attribute writable : does it have a setter
     */
    private boolean writable;

    /**
     * If this attribute is a boolean, does its getter have a 'is' prefix
     * instead of a 'get' prefix
     */
    private boolean is = false;

    /**
     * Attribute description constructor
     * 
     * @param name
     *            attribute name
     * @param description
     *            attribute description
     * @param clazz
     *            attribute class
     * @param readable
     *            is this attribute readable
     * @param writable
     *            is this attribute writable
     */
    public AccessibleFieldBean(String name, String description, Class<?> clazz, boolean readable, boolean writable)
    {
        this.name = name;
        this.description = description;
        this.className = clazz.getName();
        this.readable = readable;
        this.writable = writable;
    }

    public void setReadable()
    {
        this.readable = true;
    }

    public void setWritable()
    {
        this.writable = true;
    }

    public String getName()
    {
        return name;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;

    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isIs()
    {
        return is;
    }

    public void setIs(boolean is)
    {
        this.is = is;
    }

    public boolean getReadable()
    {
        return readable;
    }

    public boolean getWritable()
    {
        return writable;
    }

    private List<String> genericParameterClassList;

    public void setGenericParameterClassList(List<String> genericParameterClassList)
    {
        this.genericParameterClassList = genericParameterClassList;
    }

    public List<String> getGenericParameterClassList()
    {
        return genericParameterClassList;
    }

    private String declaredInClass;

    public String getDeclaredInClass()
    {
        return declaredInClass;
    }

    public void setDeclaredInClass(String declaredInClass)
    {
        this.declaredInClass = declaredInClass;
    }
}
