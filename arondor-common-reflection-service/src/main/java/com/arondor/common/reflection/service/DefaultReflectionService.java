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
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.arondor.common.reflection.api.catalog.AccessibleClassCatalog;
import com.arondor.common.reflection.api.parser.AccessibleClassParser;
import com.arondor.common.reflection.api.parser.AccessibleClassProvider;
import com.arondor.common.reflection.api.service.ReflectionService;
import com.arondor.common.reflection.model.java.AccessibleClass;
import com.arondor.common.reflection.model.java.AccessibleField;
import com.arondor.common.reflection.xstream.catalog.AccessibleClassCatalagParser;

public class DefaultReflectionService implements ReflectionService
{
    private static final Logger LOG = Logger.getLogger(DefaultReflectionService.class);

    private AccessibleClassCatalog accessibleClassCatalog;

    public AccessibleClassCatalog getAccessibleClassCatalog()
    {
        return accessibleClassCatalog;
    }

    public void setAccessibleClassCatalog(AccessibleClassCatalog accessibleClassCatalog)
    {
        this.accessibleClassCatalog = accessibleClassCatalog;
    }

    public void setCatalogFile(String resource)
    {
        setAccessibleClassCatalog(AccessibleClassCatalagParser.parseFromResource(resource));
    }

    public void setFilter(List<String> filters)
    {
        for (String filter : filters)
        {
            LOG.info("Filtering using filter '" + filter + "'");
            Pattern pattern = Pattern.compile(filter.replace("*", ".*"));
            for (AccessibleClass clazz : accessibleClassCatalog
                    .getImplementingAccessibleClasses(Object.class.getName()))
            {
                List<String> excluded = clazz.getAccessibleFields().values().stream().filter(f -> {
                    return matches(clazz, f, pattern);
                }).map(f -> {
                    return f.getName();
                }).collect(Collectors.toList());
                if (!excluded.isEmpty())
                {
                    excluded.forEach(key -> clazz.getAccessibleFields().remove(key));
                }
            }
        }
    }

    private boolean matches(AccessibleClass clazz, AccessibleField field, Pattern pattern)
    {
        String value = clazz.getName() + "." + field.getName() + ":" + field.getClassName();
        if (pattern.matcher(value).matches())
        {
            return true;
        }
        value = field.getDeclaredInClass() + "." + field.getName() + ":" + field.getClassName();
        if (pattern.matcher(value).matches())
        {
            return true;
        }
        return false;
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
        if (clazzName == null)
        {
            throw new IllegalArgumentException("Invalid null class provided !");
        }
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
