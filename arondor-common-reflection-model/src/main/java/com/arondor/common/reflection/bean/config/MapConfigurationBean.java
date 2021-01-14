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
package com.arondor.common.reflection.bean.config;

import java.util.Map;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;

public class MapConfigurationBean extends ElementConfigurationBean implements MapConfiguration
{
    /**
     * 
     */
    private static final long serialVersionUID = 2968136864852828441L;

    @Override
    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.Map;
    }

    public MapConfigurationBean()
    {

    }

    private Map<ElementConfiguration, ElementConfiguration> mapConfiguration;

    @Override
    public Map<ElementConfiguration, ElementConfiguration> getMapConfiguration()
    {
        return mapConfiguration;
    }

    @Override
    public void setMapConfiguration(Map<ElementConfiguration, ElementConfiguration> mapConfiguration)
    {
        this.mapConfiguration = mapConfiguration;
    }

    @Override
    public String toString()
    {
        return "MapConfigurationBean[" + mapConfiguration.entrySet() + "]";
    }
}
