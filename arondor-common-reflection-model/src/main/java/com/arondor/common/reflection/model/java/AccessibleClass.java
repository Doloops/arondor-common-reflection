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
package com.arondor.common.reflection.model.java;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Accessible class : Model for Java class
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleClass extends Serializable
{
    /**
     * The class name (including packages)
     * 
     * @return
     */
    public String getName();

    /**
     * The class description
     * 
     * @return
     */
    public String getDescription();

    public String getSuperclass();

    public List<String> getInterfaces();

    public List<String> getAllInterfaces();

    public List<AccessibleConstructor> getConstructors();

    public List<AccessibleMethod> getAccessibleMethods();

    public Map<String, AccessibleField> getAccessibleFields();

    public Map<String, List<String>> getAccessibleEnums();

    public String getClassBaseName();

    public String getPackageName();

    public boolean isAbstract();

}
