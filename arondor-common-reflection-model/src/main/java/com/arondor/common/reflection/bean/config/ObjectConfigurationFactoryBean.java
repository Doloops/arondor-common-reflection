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
