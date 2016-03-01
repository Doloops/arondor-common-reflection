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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;
import com.arondor.common.reflection.noreflect.model.FieldSetter;
import com.arondor.common.reflection.noreflect.model.ObjectConstructor;
import com.arondor.common.reflection.noreflect.model.ObjectConstructorAsync;
import com.arondor.common.reflection.noreflect.model.PackageInstantiatorAsync;
import com.arondor.common.reflection.noreflect.model.ReflectionInstantiatorCatalog;

public class SimpleReflectionInstantiatorCatalog implements ReflectionInstantiatorCatalog
{
    private final static Logger LOG = Logger.getLogger(SimpleReflectionInstantiatorCatalog.class.getName());

    private final Map<String, String[]> inheritanceMap = new HashMap<String, String[]>();

    private final Map<String, ObjectConstructor> objectConstructorMap = new HashMap<String, ObjectConstructor>();

    private final Map<String, ObjectConstructorAsync> objectConstructorAsyncMap = new HashMap<String, ObjectConstructorAsync>();

    private final Map<String, FieldSetter> fieldSetterMap = new HashMap<String, FieldSetter>();

    private final Map<String, PackageInstantiatorAsync> packageInstantiatorMap = new HashMap<String, PackageInstantiatorAsync>();

    private final Map<String, String> class2package = new HashMap<String, String>();

    @Override
    public void registerObjectInheritance(String className, String inheritance[])
    {
        inheritanceMap.put(className, inheritance);
    }

    @Override
    public void registerObjectConstructor(String name, ObjectConstructor objectConstructor)
    {
        LOG.finest("registerObjectConstructor(" + name + ", ...)");
        objectConstructorMap.put(name, objectConstructor);
    }

    @Override
    public void registerObjectConstructor(String name, ObjectConstructorAsync objectConstructor)
    {
        LOG.finest("registerObjectConstructor(" + name + ", ...)");
        objectConstructorAsyncMap.put(name, objectConstructor);
    }

    @Override
    public void registerFieldSetter(String className, String fieldName, FieldSetter fieldSetter)
    {
        fieldSetterMap.put(getFieldSetterName(className, fieldName), fieldSetter);
    }

    private String getFieldSetterName(String className, String fieldName)
    {
        return className + "." + fieldName;
    }

    @Override
    public ObjectConstructor getObjectConstructor(String className)
    {
        return objectConstructorMap.get(className);
    }

    @Override
    public void getObjectConstructorAsync(String className, InstantiationCallback<ObjectConstructor> callback)
    {
        ObjectConstructor sync = objectConstructorMap.get(className);
        if (sync != null)
        {
            callback.onSuccess(sync);
            return;
        }
        ObjectConstructorAsync async = objectConstructorAsyncMap.get(className);
        if (async == null)
        {
            callback.onFailure(new IllegalArgumentException("No async callback for :" + className));
        }
        async.getObjectConstructor(callback);
    }

    @Override
    public FieldSetter getFieldSetter(String className, String fieldName)
    {
        FieldSetter fieldSetter = fieldSetterMap.get(getFieldSetterName(className, fieldName));
        if (fieldSetter != null)
        {
            return fieldSetter;
        }
        String inheritance[] = inheritanceMap.get(className);
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

    @Override
    public void registerPackageInstantiator(String packageName, PackageInstantiatorAsync instantiator)
    {
        packageInstantiatorMap.put(packageName, instantiator);
    }

    @Override
    public PackageInstantiatorAsync getPackageInstantiator(String packageName)
    {
        return packageInstantiatorMap.get(packageName);
    }

    @Override
    public void registerClassInPackage(String packageName, String className)
    {
        class2package.put(className, packageName);
    }

    @Override
    public String getPackageForClass(String className)
    {
        return class2package.get(className);
    }
}
