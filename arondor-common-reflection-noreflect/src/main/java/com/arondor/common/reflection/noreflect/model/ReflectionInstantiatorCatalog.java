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
package com.arondor.common.reflection.noreflect.model;

import java.util.Collection;

import com.arondor.common.reflection.api.instantiator.InstantiationCallback;

/**
 * Runtime hardwired Object reflection catalog : Create objects and Set fields
 * 
 * @author Francois Barre
 * 
 */
public interface ReflectionInstantiatorCatalog
{
    void registerObjectInheritance(String className, Collection<String> inheritance);

    void registerObjectConstructor(String className, ObjectConstructor objectConstructor);

    void registerObjectConstructor(String className, ObjectConstructorAsync objectConstructor);

    void registerFieldSetter(String className, String fieldName, FieldSetter fieldSetter);

    ObjectConstructor getObjectConstructor(String className);

    FieldSetter getFieldSetter(String className, String fieldName);

    void getObjectConstructorAsync(String className, InstantiationCallback<ObjectConstructor> callback);

    /**
     * 
     * @param moduleName
     * @param callback
     * 
     * @since 0.3.1
     */
    void registerPackageInstantiator(String moduleName, PackageInstantiatorAsync callback);

    /**
     * 
     * @param moduleName
     * @return
     * 
     * @since 0.3.1
     */
    PackageInstantiatorAsync getPackageInstantiator(String moduleName);

    /**
     * @param packageName
     * @param className
     * @since 0.3.1
     */
    void registerClassInPackage(String packageName, String className);

    /**
     * 
     * @param className
     * @return
     * 
     * @since 0.3.1
     */
    String getPackageForClass(String className);
}
