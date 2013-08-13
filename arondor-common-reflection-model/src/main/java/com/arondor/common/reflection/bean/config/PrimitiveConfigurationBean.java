package com.arondor.common.reflection.bean.config;

import com.arondor.common.reflection.model.config.PrimitiveConfiguration;

public class PrimitiveConfigurationBean extends ElementConfigurationBean implements PrimitiveConfiguration
{
    /**
     * 
     */
    private static final long serialVersionUID = 6752703137456191989L;

    public ElementConfigurationType getFieldConfigurationType()
    {
        return ElementConfigurationType.Primitive;
    }

    public PrimitiveConfigurationBean()
    {

    }

    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

}
