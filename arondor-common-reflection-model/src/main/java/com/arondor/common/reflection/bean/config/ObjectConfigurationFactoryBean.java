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

import java.util.ArrayList;

import com.arondor.common.reflection.model.config.ElementConfiguration;
import com.arondor.common.reflection.model.config.ListConfiguration;
import com.arondor.common.reflection.model.config.MapConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;
import com.arondor.common.reflection.model.config.PrimitiveConfiguration;
import com.arondor.common.reflection.model.config.ReferenceConfiguration;
import com.arondor.common.reflection.util.StrongReference;

public class ObjectConfigurationFactoryBean implements ObjectConfigurationFactory
{

    @Override
    public ObjectConfiguration createObjectConfiguration()
    {
        return new ObjectConfigurationBean();
    }

    @Override
    public ObjectConfigurationMap createObjectConfigurationMap()
    {
        return new ObjectConfigurationMapBean();
    }

    @Override
    public PrimitiveConfiguration createPrimitiveConfiguration()
    {
        return new PrimitiveConfigurationBean();
    }

    @Override
    public PrimitiveConfiguration createPrimitiveConfiguration(String value)
    {
        PrimitiveConfiguration primitiveConfiguration = createPrimitiveConfiguration();
        primitiveConfiguration.setValue(value);
        return primitiveConfiguration;
    }

    @Override
    public ListConfiguration createListConfiguration()
    {
        return new ListConfigurationBean();
    }

    @Override
    public MapConfiguration createMapConfiguration()
    {
        return new MapConfigurationBean();
    }

    @Override
    public ReferenceConfiguration createReferenceConfiguration()
    {
        return new ReferenceConfigurationBean();
    }

    @Override
    public ObjectConfiguration createObjectConfigurationFromReference(ReferenceConfiguration referenceConfiguration)
    {
        ObjectConfigurationBean objectConfigurationBean = new ObjectConfigurationBean();
        objectConfigurationBean.setClassName(StrongReference.CLASSNAME);
        objectConfigurationBean.setConstructorArguments(new ArrayList<ElementConfiguration>());
        objectConfigurationBean.getConstructorArguments().add(referenceConfiguration);
        return objectConfigurationBean;
    }
}
