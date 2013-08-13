package com.arondor.common.reflection.model.config;

/**
 * Construct Elements
 * 
 * @author Francois Barre
 * 
 */
public interface ObjectConfigurationFactory
{
    ObjectConfiguration createObjectConfiguration();

    FieldConfiguration createFieldConfiguration();

    ObjectConfigurationMap createObjectConfigurationMap();
}
