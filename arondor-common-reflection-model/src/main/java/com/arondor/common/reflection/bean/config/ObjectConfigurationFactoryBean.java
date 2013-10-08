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

import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;

public class ObjectConfigurationFactoryBean implements ObjectConfigurationFactory
{

    public ObjectConfiguration createObjectConfiguration()
    {
        return new ObjectConfigurationBean();
    }

    public ObjectConfigurationMap createObjectConfigurationMap()
    {
        return new ObjectConfigurationMapBean();
    }

    public PrimitiveConfiguration createPrimitiveConfiguration()
    {
        return new PrimitiveConfigurationBean();
    }

    public PrimitiveConfiguration createPrimitiveConfiguration(String value)
    {
        PrimitiveConfiguration primitiveConfiguration = createPrimitiveConfiguration();
        primitiveConfiguration.setValue(value);
        return primitiveConfiguration;
    }

    public ListConfiguration createListConfiguration()
    {
        return new ListConfigurationBean();
    }

    public MapConfiguration createMapConfiguration()
    {
        return new MapConfigurationBean();
    }

    public ReferenceConfiguration createReferenceConfiguration()
    {
        return new ReferenceConfigurationBean();
    }
}
