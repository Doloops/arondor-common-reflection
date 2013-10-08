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
package com.arondor.common.reflection.api.parser;

import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.util.PrimitiveTypeUtil;

/**
 * Parse a Java-loaded class to an AccessibleClass
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleClassParser
{
    /**
     * Parses a Java-loaded class object to an AccessibleClass
     * 
     * @param clazz
     *            the class to parse
     * @return the parsed AccessibleClass
     */
    AccessibleClass parseAccessibleClass(Class<?> clazz);

    /**
     * Check whever the provided class name is said to be primitive
     * (Primitive_Single) or not (Object_Single)
     * 
     * @param className
     *            the class name
     * @return true if the type is primitive, false otherwise
     * @deprecated @see {@link PrimitiveTypeUtil}
     */
    @Deprecated
    boolean isPrimitiveType(String className);

    /**
     * Give the setter method name according to the field name
     * 
     * @param name
     *            the filed name
     * @return the setter name
     */
    String attributeToSetter(String name);

    /**
     * Give the getter method name according to the field name
     * 
     * @param name
     *            the field name
     * @return the getter name
     */
    String attributeToGetter(String name);

    /**
     * Give the getter method name according to the field name for boolean
     * values (is*())
     * 
     * @param name
     *            the boolean field name
     * @return the getter name
     */
    String booleanAttributeToGetter(String arg0);
}
