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
package com.arondor.common.reflection.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.bean.java.AccessibleClassBean;
import com.arondor.common.reflection.bean.java.AccessibleFieldBean;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleConstructor;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.model.java.AccessibleMethod;

/**
 * Simple Class Catalog Parser
 * 
 * @author francois
 * 
 */
public class SimpleAccessibleClassCatalog implements AccessibleClassCatalog
{
    private final Map<String, AccessibleClass> accessibleClassMap = new HashMap<String, AccessibleClass>();

    public SimpleAccessibleClassCatalog()
    {
        AccessibleClassBean stringClass = new AccessibleClassBean();
        stringClass.setName(String.class.getName());
        stringClass.setConstructors(new ArrayList<AccessibleConstructor>());
        stringClass.setAccessibleMethods(new ArrayList<AccessibleMethod>());
        stringClass.setAccessibleFields(new HashMap<String, AccessibleField>());
        stringClass.setSuperclass(Object.class.getName());
        stringClass.setInterfaces(new ArrayList<String>());
        stringClass.setAllInterfaces(new ArrayList<String>());
        stringClass.getAllInterfaces().add(Object.class.getName());

        AccessibleFieldBean stringValue = new AccessibleFieldBean("value", "The String value", String.class, true, true);
        stringClass.getAccessibleFields().put(stringValue.getName(), stringValue);

        accessibleClassMap.put(stringClass.getName(), stringClass);
    }

    private boolean allowSuperclassesInImplementingClases = false;

    public boolean isAllowSuperclassesInImplementingClases()
    {
        return allowSuperclassesInImplementingClases;
    }

    public void setAllowSuperclassesInImplementingClases(boolean allowSuperclassesInImplementingClases)
    {
        this.allowSuperclassesInImplementingClases = allowSuperclassesInImplementingClases;
    }

    public void addAccessibleClass(AccessibleClass accessibleClass)
    {
        accessibleClassMap.put(accessibleClass.getName(), accessibleClass);
    }

    public AccessibleClass getAccessibleClass(final String className) throws ClassNotFoundException
    {
        String name = className;
        AccessibleClass accessibleClass = accessibleClassMap.get(name);
        if (accessibleClass == null)
        {
            throw new ClassNotFoundException("No class " + name + " defined in catalog !");
        }
        return accessibleClass;
    }

    public Collection<AccessibleClass> getImplementingAccessibleClasses(String desiredInterfaceName)
    {
        List<AccessibleClass> result = new ArrayList<AccessibleClass>();
        for (AccessibleClass clazz : accessibleClassMap.values())
        {
            for (String interfaceName : clazz.getAllInterfaces())
            {
                if (desiredInterfaceName.equals(interfaceName))
                {
                    result.add(clazz);
                    break;
                }
            }
            if (isAllowSuperclassesInImplementingClases())
            {
                if (clazz.getSuperclass().equals(desiredInterfaceName))
                {
                    result.add(clazz);
                }
                else if (clazz.getName().equals(desiredInterfaceName))
                {
                    result.add(clazz);
                }
            }
        }
        return result;
    }
}