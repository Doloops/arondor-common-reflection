package com.arondor.common.reflection.api.catalog;

import java.util.Collection;

import com.arondor.common.reflection.model.java.AccessibleClass;

/**
 * Interface for parsed AccessibleClass catalog
 * 
 * @author Francois Barre
 * 
 */
public interface AccessibleClassCatalog
{
    /**
     * Add a parsed AccessibleClass to the given Catalog
     * 
     * @param accessibleClass
     *            the parsed AccessibleClass
     */
    void addAccessibleClass(AccessibleClass accessibleClass);

    /**
     * Get an AccessibleClass by its name
     * 
     * @param className
     *            name of the class to retrieve
     * @return the AccessibleClass
     */
    AccessibleClass getAccessibleClass(String className) throws ClassNotFoundException;

    /**
     * Get all parsed AccessibleClass implementing a given interface
     * 
     * @param the
     *            interface class
     * @return the list of classes implementing this interface
     */
    Collection<AccessibleClass> getImplementingAccessibleClasses(String interfaceClass);
}
