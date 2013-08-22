package com.arondor.common.reflection.bean.config;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.arondor.common.reflection.model.config.PrimitiveConfiguration;

@Entity
@DiscriminatorValue("PRIM")
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

    @Override
    public String toString()
    {
        return "PrimitiveConfigurationBean [value=" + value + "]";
    }

}
