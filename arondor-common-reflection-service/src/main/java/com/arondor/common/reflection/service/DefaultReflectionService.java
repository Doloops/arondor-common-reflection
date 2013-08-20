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

    public AccessibleClass getAccessibleClass(String clazzName) throws RemoteException
    {
        if (getAccessibleClassCatalog() != null)
        {
            try
            {
                return getAccessibleClassCatalog().getAccessibleClass(clazzName);
            }
            catch (ClassNotFoundException e)
            {
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
