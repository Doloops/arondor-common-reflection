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
package com.arondor.common.reflection.api.catalog;

import java.util.Collection;

import com.arondor.common.reflection.model.java.AccessibleClass;

/**
 * Interface for parsed AccessibleClass catalog.
 * 
 * Package should be renamed to something else than .api.
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
