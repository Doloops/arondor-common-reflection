package com.arondor.common.reflection.api.instantiator;

import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

/**
 * Instantiation Context, holding shared objects and shared object
 * configurations
 * 
 * @author Francois Barre
 * 
 */
public interface InstantiationContext
{
    public void putSharedObject(String name, Object reference);

    public Object getSharedObject(String name);

    public ObjectConfiguration getSharedObjectConfiguration(String name);

    public void setSharedObjectConfigurations(ObjectConfigurationMap objetConfigurations);

    public void addSharedObjectResolver(SharedObjectResolver sharedObjectResolver);
}
