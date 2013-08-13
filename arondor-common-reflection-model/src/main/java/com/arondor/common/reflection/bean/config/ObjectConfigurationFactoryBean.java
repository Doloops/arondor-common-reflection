package com.arondor.common.reflection.bean.config;

import com.arondor.common.reflection.model.config.FieldConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfiguration;
import com.arondor.common.reflection.model.config.ObjectConfigurationFactory;
import com.arondor.common.reflection.model.config.ObjectConfigurationMap;

public class ObjectConfigurationFactoryBean implements ObjectConfigurationFactory
{

    public ObjectConfiguration createObjectConfiguration()
    {
        return new ObjectConfigurationBean();
    }

    public FieldConfiguration createFieldConfiguration()
    {
        return new FieldConfigurationBean();
    }

    public ObjectConfigurationMap createObjectConfigurationMap()
    {
        return new ObjectConfigurationMapBean();
    }
}
