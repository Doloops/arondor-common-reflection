package com.arondor.common.reflection.api.parser;

import com.arondor.common.reflection.model.java.AccessibleClass;

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
     */
    boolean isPrimitiveType(String className);

    /**
     * Give the setter method name according the
     * 
     * @param name
     * @return
     */
    String attributeToSetter(String name);
}
