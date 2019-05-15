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
package com.arondor.common.reflection.service;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.api.parser.AccessibleClassProvider;
import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.model.java.AccessibleClass;

public class DefaultReflectionService implements ReflectionService
{
    private AccessibleClassCatalog accessibleClassCatalog;

    public AccessibleClassCatalog getAccessibleClassCatalog()
    {
        return accessibleClassCatalog;
    }

    public void setAccessibleClassCatalog(AccessibleClassCatalog accessibleClassCatalog)
    {
        this.accessibleClassCatalog = accessibleClassCatalog;
    }

    private AccessibleClassParser accessibleClassParser;

    public AccessibleClassParser getAccessibleClassParser()
    {
        return accessibleClassParser;
    }

    public void setAccessibleClassParser(AccessibleClassParser accessibleClassParser)
    {
        this.accessibleClassParser = accessibleClassParser;
    }

    @Override
    public AccessibleClass getAccessibleClass(String clazzName) throws RemoteException
    {
        if (getAccessibleClassCatalog() != null)
        {
            AccessibleClass clazz = getAccessibleClassCatalog().getAccessibleClass(clazzName);
            if (clazz != null)
            {
                return clazz;
            }
        }
        Class<?> clazz;
        try
        {
            clazz = Class.forName(clazzName);
            return getAccessibleClassParser().parseAccessibleClass(clazz);
        }
        catch (ClassNotFoundException e)
        {
            throw new RemoteException("Could not get class", e);
        }
    }

    @Override
    public Collection<AccessibleClass> getImplementingAccessibleClasses(String name) throws RemoteException
    {
        return getAccessibleClassCatalog().getImplementingAccessibleClasses(name);
    }

    public void setAccessibleClassProviders(List<AccessibleClassProvider> providers)
    {
        for (AccessibleClassProvider provider : providers)
        {
            provider.provideClasses(getAccessibleClassCatalog());
        }
    }
}
