package com.arondor.common.reflection.model.config;

import java.util.Map;

public interface MapConfiguration extends ElementConfiguration
{
    Map<ElementConfiguration, ElementConfiguration> getMapConfiguration();

    void setMapConfiguration(Map<ElementConfiguration, ElementConfiguration> mapConfiguration);
}
