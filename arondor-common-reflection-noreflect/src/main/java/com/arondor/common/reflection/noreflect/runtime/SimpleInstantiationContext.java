package com.arondor.common.reflection.noreflect.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arondor.common.reflection.api.instantiator.InstantiationContext;
import com.arondor.common.reflection.api.instantiator.SharedObjectResolver;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

public class SimpleInstantiationContext implements InstantiationContext
{
    private Map<String, Object> instances = new HashMap<String, Object>();

    public Object getSharedObject(String name)
    {
        Object object = instances.get(name);
        if (object != null)
        {
            return object;
        }
        for (SharedObjectResolver resolver : sharedObjectResolvers)
        {
            object = resolver.getSharedObject(name);
            if (object != null)
            {
                return object;
            }
        }
        return null;
    }

    public void putSharedObject(String name, Object reference)
    {
        instances.put(name, reference);
    }

    public ObjectConfiguration getSharedObjectConfiguration(String name)
    {
        return objetConfigurations.get(name);
    }

    private final Map<String, ObjectConfiguration> objetConfigurations = new HashMap<String, ObjectConfiguration>();

    public void setSharedObjectConfigurations(ObjectConfigurationMap objetConfigurations)
    {
        addSharedObjectConfigurations(objetConfigurations);
    }

    public void addSharedObjectConfigurations(ObjectConfigurationMap objetConfigurations)
    {
        for (Map.Entry<String, ObjectConfiguration> entry : objetConfigurations.entrySet())
        {
            this.objetConfigurations.put(entry.getKey(), entry.getValue());
        }
    }

    private List<SharedObjectResolver> sharedObjectResolvers = new ArrayList<SharedObjectResolver>();

    public void addSharedObjectResolver(SharedObjectResolver sharedObjectResolver)
    {
        sharedObjectResolvers.add(sharedObjectResolver);
    }

}
