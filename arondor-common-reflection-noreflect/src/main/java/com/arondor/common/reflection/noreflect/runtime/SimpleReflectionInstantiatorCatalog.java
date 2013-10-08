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
package com.arondor.common.reflection.noreflect.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;

public class SimpleReflectionInstantiatorCatalog implements ReflectionInstantiatorCatalog
{
    private final static Logger LOG = Logger.getLogger(SimpleReflectionInstantiatorCatalog.class.getName());

    private final Map<String, Collection<String>> inheritanceMap = new HashMap<String, Collection<String>>();

    private final Map<String, ObjectConstructor> objectConstructorMap = new HashMap<String, ObjectConstructor>();

    private final Map<String, FieldSetter> fieldSetterMap = new HashMap<String, FieldSetter>();

    public void registerObjectInheritance(String className, Collection<String> inheritance)
    {
        inheritanceMap.put(className, inheritance);
    }

    public void registerObjectConstructor(String name, ObjectConstructor objectConstructor)
    {
        LOG.finest("registerObjectConstructor(" + name + ", ...)");
        objectConstructorMap.put(name, objectConstructor);
    }

    public void registerFieldSetter(String className, String fieldName, FieldSetter fieldSetter)
    {
        fieldSetterMap.put(getFieldSetterName(className, fieldName), fieldSetter);
    }

    private String getFieldSetterName(String className, String fieldName)
    {
        return className + "." + fieldName;
    }

    public ObjectConstructor getObjectConstructor(String className)
    {
        return objectConstructorMap.get(className);
    }

    public FieldSetter getFieldSetter(String className, String fieldName)
    {
        FieldSetter fieldSetter = fieldSetterMap.get(getFieldSetterName(className, fieldName));
        if (fieldSetter != null)
        {
            return fieldSetter;
        }
        Collection<String> inheritance = inheritanceMap.get(className);
        if (inheritance != null)
        {
            for (String parent : inheritance)
            {
                fieldSetter = getFieldSetter(parent, fieldName);
                if (fieldSetter != null)
                {
                    return fieldSetter;
                }
            }
        }
        return null;
    }
}
