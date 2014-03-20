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

/**
 * Accessible Field : Model for Java class Field
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleField extends Serializable
{
    /**
     * @return the field name
     */
    String getName();

    /**
     * 
     * @return the field description
     */
    String getDescription();

    /**
     * 
     * @return the field Class name
     */
    String getClassName();

    /**
     * The generic Parameter class list for generics
     * 
     * @return
     */
    List<String> getGenericParameterClassList();

    /**
     * Is the field readable (with a get()) ?
     * 
     * @return true if the field is readable, false otherwise
     */
    boolean getReadable();

    /**
     * Is the field writable (with a set()) ?
     * 
     * @return true if the field is writable, false otherwise
     */
    boolean getWritable();

    /**
     * Is the field mandatory ?
     * 
     * @return true if the field is mandatory, false otherwise
     */
    boolean isMandatory();

    /**
     * Get default value of field (for primitive : String, Integer,Float,
     * Boolean)
     * 
     * @return
     */
    String getDefaultValue();

    /**
     * Is the field bind to enum type
     * 
     * @return
     */
    boolean isEnumProperty();

    /**
     * For booleans, we prefer declaring boolean isField() instead of boolean
     * getField() ; it is a matter of taste actually
     * 
     * @return
     */
    @Deprecated
    boolean isIs();

    /**
     * In which class this field is declared
     */
    String getDeclaredInClass();

}
