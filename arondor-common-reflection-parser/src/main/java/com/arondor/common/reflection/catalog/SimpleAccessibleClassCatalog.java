package com.arondor.common.reflection.catalog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.api.accessiblecatalog.AccessibleClassCatalog;
import com.arondor.common.reflection.model.java.AccessibleClass;

/**
 * Simple Class Catalog Parser
 * 
 * @author francois
 * 
 */
public class SimpleAccessibleClassCatalog implements AccessibleClassCatalog
{

    private Map<String, AccessibleClass> accessibleClassMap = new HashMap<String, AccessibleClass>();

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
        }
        return result;
    }

}
