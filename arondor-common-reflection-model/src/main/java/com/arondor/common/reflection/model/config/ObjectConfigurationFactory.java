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

    PrimitiveConfiguration createPrimitiveConfiguration();

    PrimitiveConfiguration createPrimitiveConfiguration(String value);

    ListConfiguration createListConfiguration();

    MapConfiguration createMapConfiguration();

    ObjectConfigurationMap createObjectConfigurationMap();

    ReferenceConfiguration createReferenceConfiguration();
}
